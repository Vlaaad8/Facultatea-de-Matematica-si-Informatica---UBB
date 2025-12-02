import express from "express";
import cors from "cors"
import {MovieRepository} from "./movieRepository";
import {logger} from "./logger";
import { WebSocket } from 'ws';
import expressWs from "express-ws";
import {Express} from "express"
import {Movie} from "./model/movie";
import {authenticateJWT} from "./jwt";
import jwt from "jsonwebtoken";
import path from "path";
import fs from "fs";


interface ExpressWithWs extends Express {
    ws: (path: string, callback: (ws: WebSocket, req: express.Request) => void) => void;
}

export class RestService {

    private app: ExpressWithWs ;
    private port: number = 8081;
    private movieRepository = new MovieRepository();
    private appW!: ReturnType<typeof expressWs>;
    private uploadDir = path.join(__dirname, "..", "uploads");

    constructor() {
        this.app=express() as ExpressWithWs;
        this.app.use(cors())
        this.appW = expressWs(this.app);
        this.app.use(express.json({limit: "10mb"}));
        this.ensureUploadDirectory();
        this.app.use("/uploads", express.static(this.uploadDir));
        this.app.use((req, res, next) => {
            if (req.path === "/" || req.path === "/login" || req.path === "/ws/.websocket") {
                return next();
            }
            return authenticateJWT(req, res, next);
        });

        this.getMovies();
        this.getMovie()
        this.webSocket();
        this.login();
        this.addMovie();
        this.updateMovie();
        this.uploadPhoto();
        this.app.listen(this.port, () => {
            logger.log(`Server started on port ${this.port}`)
        })

    }

    private ensureUploadDirectory(): void {
        if (!fs.existsSync(this.uploadDir)) {
            fs.mkdirSync(this.uploadDir, {recursive: true});
        }
    }


    public getMovies(): void {
        this.app.get("/movies", authenticateJWT,async (req: express.Request, res: express.Response) => {
            try {
                let pageNumber = parseInt(req.query.pageNumber as string);
                let pageSize = parseInt(req.query.pageSize as string);

                const user = (req as any).user;
                const userId = user.id || user.userId;

                if (!userId) {
                    return res.status(401).send({ error: "User ID missing in token" });
                }

                logger.info(`User ${userId} requested movies with pageNumber=${pageNumber} and pageSize=${pageSize}`);

                const movies = await this.movieRepository.getMovies(userId, pageNumber, pageSize);
                res.send(movies);
            } catch (err) {
                res.status(500).send({error: "Failed to get movies! Check the API or server!"})
            }
        })
    }

    public getMovie(): void {
        this.app.get(`/movies/:id`, authenticateJWT,async (req: express.Request, res: express.Response) => {

            let id = parseInt(req.params.id);
            logger.info(`A request came to get movie with id=${id}`)
            try {
                const movie = await this.movieRepository.getMovie(id);
                if(movie) {
                    res.send(movie)

                }
                else{
                    res.status(400).send({error: `No movie found with such id=${id}!`})
                }
            } catch (err) {
                res.status(500).send({error: "Failed to get movie with id " + id})
            }
        })
    }

    public webSocket() {
        this.app.ws('/ws', (ws: WebSocket, req: express.Request) => {
            logger.info('Client attempting WS connection');

            const token = typeof req.query.token === 'string' ? req.query.token : '';
            if (!token) {
                logger.error('Missing JWT token in WS connection');
                ws.send(JSON.stringify({ type: 'error', message: 'Missing token' }));
                ws.close();
                return;
            }

            jwt.verify(token, "d3b5c4c8539dfcfcc883b6fd63bbdb23", (err, decoded) => {
                if (err) {
                    logger.error('Invalid or expired token');
                    ws.send(JSON.stringify({ type: 'error', message: 'Invalid or expired token' }));
                    ws.close();
                    return;
                }

                const user = decoded as { id?: number; userId?: number; [key: string]: any };
                (ws as any).user = user;
                logger.info(`WebSocket connected and authenticated for user ${user.id || user.userId}`);

                ws.on('message', (msg: string) => {
                    logger.info(`Message from user ${user.id || user.userId}: ${msg}`);
                });

                ws.on('close', () => {
                    logger.info(`WebSocket closed for user ${user.id || user.userId}`);
                });

            });
        });
    }


    public login(){
        this.app.post("/login", async (req: express.Request, res: express.Response) => {
            const {username, password }= req.body;
            logger.debug(req.body);
            logger.info(`A login request with username= ${username} and passoword= ${password}`);
            try{
                const token= await this.movieRepository.login(username, password);
                if(token==""){
                    return res.status(401).json({error: "No user found!"})
                }
                else{
                    return res.status(200).json({token: token});
                }
            }
            catch(error){
                return res.status(500).json({error: "Failed to login!"})
            }
        })
    }

    public addMovie(){
        this.app.post("/", authenticateJWT, async (req: express.Request, res: express.Response) => {
            let movie : Movie = req.body
            try{
                logger.info("Adding a new resource")
                const user = (req as any).user;
                const userId = user.id || user.userId;

                if (!userId) {
                    return res.status(401).send({ error: "User ID missing in token" });
                }
                movie.owner_id = userId;
                const added = await this.movieRepository.addMovie(movie)

                this.appW.getWss().clients.forEach((client: any) => {
                    logger.debug("Starting to send notifications")
                    if (client.readyState === WebSocket.OPEN) {
                        const clientUser = client.user;
                        logger.debug(clientUser.id)
                        if (clientUser && clientUser.id  === userId) {
                            client.send(JSON.stringify(added));
                            logger.info(`Sent new movie to user ${userId}: ${JSON.stringify(added)}`);
                        }
                    }
                });
                res.send(added)
            }
            catch(error){
                console.error(error);
                res.status(500).send({error: "Failed to add movie to the user!"})
            }
        })
    }

    public updateMovie() {
        this.app.put("/movies/:id", authenticateJWT, async (req: express.Request, res: express.Response) => {
            try {
                const user = (req as any).user;
                const userId = user.id || user.userId;
                if (!userId) {
                    return res.status(401).send({ error: "User ID missing in token" });
                }

                const movie : Movie = req.body;
                movie.id = Number(req.params.id);
                movie.owner_id = userId;

                const updated = await this.movieRepository.updateMovie(movie);
                res.send(updated);
            } catch (error) {
                logger.error(error);
                res.status(500).send({error: "Failed to update movie!"})
            }
        })
    }

    public uploadPhoto() {
        this.app.post("/upload", authenticateJWT, async (req: express.Request, res: express.Response) => {
            const { data, fileName } = req.body;
            if (!data) {
                return res.status(400).send({error: "Missing photo data"});
            }

            try {
                const base64Data = data.includes("base64,") ? data.split("base64,")[1] : data;
                const buffer = Buffer.from(base64Data, "base64");
                const safeName = fileName || `movie_${Date.now()}.jpeg`;
                const targetPath = path.join(this.uploadDir, safeName);
                await fs.promises.writeFile(targetPath, buffer);
                const url = `${req.protocol}://${req.get("host")}/uploads/${safeName}`;
                res.send({photoUrl: url, photoPath: safeName});
            } catch (error) {
                logger.error(error);
                res.status(500).send({error: "Failed to upload photo"});
            }
        })
    }

}