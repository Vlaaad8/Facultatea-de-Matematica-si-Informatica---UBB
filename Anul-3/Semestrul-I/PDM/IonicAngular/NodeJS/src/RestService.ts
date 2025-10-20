import express from "express";
import cors from "cors"
import {MovieRepository} from "./movieRepository";
import {logger} from "./logger";
import { WebSocket } from 'ws';
import expressWs from "express-ws";
import {Express} from "express"


interface ExpressWithWs extends Express {
    ws: (path: string, callback: (ws: WebSocket, req: express.Request) => void) => void;
}

export class RestService {

    private app: ExpressWithWs ;
    private port: number = 8080;
    private movieRepository = new MovieRepository();
    private appW!: ReturnType<typeof expressWs>;

    constructor() {
        this.app=express() as ExpressWithWs;
        this.app.use(cors())
        this.appW = expressWs(this.app);
        this.sayHi();
        this.getMovies();
        this.getMovie()
        this.webSocket();
        this.app.listen(this.port, () => {
            logger.log(`Server started on port ${this.port}`)
        })

    }

    public sayHi(): void {
        this.app.get("/", (req: express.Request, res: express.Response) => {
            res.send("Hi");
        })
    }

    public getMovies(): void {
        this.app.get("/movies", async (req: express.Request, res: express.Response) => {
            try {
                let pageNumber = parseInt(req.query.pageNumber as string);
                let pageSize = parseInt(req.query.pageSize as string);
                logger.info(`A user requested movies with pageNumber=${pageNumber} and pageSize=${pageSize}`);
                const movies = await this.movieRepository.getMovies(pageNumber, pageSize);
                res.send(movies);
            } catch (err) {
                res.status(500).send({error: "Failed to get movies! Check the API or server!"})
            }
        })
    }

    public getMovie(): void {
        this.app.get(`/movies/:id`, async (req: express.Request, res: express.Response) => {

            let id = parseInt(req.params.id);
            logger.info(`A request came to get movie with id=${id}`)
            try {
                const movie = await this.movieRepository.getMovie(id);
                if(movie) {
                    this.appW.getWss().clients.forEach((client : WebSocket) => {
                        if(client.readyState === WebSocket.OPEN) {
                            client.send(JSON.stringify({title: 'New interested client!',message: movie.name }))
                        }
                    })
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
        this.app.ws('/ws', (ws: WebSocket) => {
            logger.info('Client connected');

            ws.on('message', (msg: string) => {
                logger.info('Received:', msg.toString());
            });

            ws.send(JSON.stringify({type: 'info', message: 'Welcome!'}));
        });


    }
}