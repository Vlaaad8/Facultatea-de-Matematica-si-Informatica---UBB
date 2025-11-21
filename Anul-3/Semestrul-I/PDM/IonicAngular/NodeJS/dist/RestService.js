"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.RestService = void 0;
const express_1 = __importDefault(require("express"));
const cors_1 = __importDefault(require("cors"));
const movieRepository_1 = require("./movieRepository");
const logger_1 = require("./logger");
const ws_1 = require("ws");
const express_ws_1 = __importDefault(require("express-ws"));
const jwt_1 = require("./jwt");
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
const path_1 = __importDefault(require("path"));
const fs_1 = __importDefault(require("fs"));
class RestService {
    constructor() {
        this.port = 8081;
        this.movieRepository = new movieRepository_1.MovieRepository();
        this.uploadDir = path_1.default.join(__dirname, "..", "uploads");
        this.app = (0, express_1.default)();
        this.app.use((0, cors_1.default)());
        this.appW = (0, express_ws_1.default)(this.app);
        this.app.use(express_1.default.json({ limit: "10mb" }));
        this.ensureUploadDirectory();
        this.app.use("/uploads", express_1.default.static(this.uploadDir));
        this.app.use((req, res, next) => {
            if (req.path === "/" || req.path === "/login" || req.path === "/ws/.websocket") {
                return next();
            }
            return (0, jwt_1.authenticateJWT)(req, res, next);
        });
        this.getMovies();
        this.getMovie();
        this.webSocket();
        this.login();
        this.addMovie();
        this.updateMovie();
        this.uploadPhoto();
        this.app.listen(this.port, () => {
            logger_1.logger.log(`Server started on port ${this.port}`);
        });
    }
    ensureUploadDirectory() {
        if (!fs_1.default.existsSync(this.uploadDir)) {
            fs_1.default.mkdirSync(this.uploadDir, { recursive: true });
        }
    }
    getMovies() {
        this.app.get("/movies", jwt_1.authenticateJWT, async (req, res) => {
            try {
                let pageNumber = parseInt(req.query.pageNumber);
                let pageSize = parseInt(req.query.pageSize);
                const user = req.user;
                const userId = user.id || user.userId;
                if (!userId) {
                    return res.status(401).send({ error: "User ID missing in token" });
                }
                logger_1.logger.info(`User ${userId} requested movies with pageNumber=${pageNumber} and pageSize=${pageSize}`);
                const movies = await this.movieRepository.getMovies(userId, pageNumber, pageSize);
                res.send(movies);
            }
            catch (err) {
                res.status(500).send({ error: "Failed to get movies! Check the API or server!" });
            }
        });
    }
    getMovie() {
        this.app.get(`/movies/:id`, jwt_1.authenticateJWT, async (req, res) => {
            let id = parseInt(req.params.id);
            logger_1.logger.info(`A request came to get movie with id=${id}`);
            try {
                const movie = await this.movieRepository.getMovie(id);
                if (movie) {
                    res.send(movie);
                }
                else {
                    res.status(400).send({ error: `No movie found with such id=${id}!` });
                }
            }
            catch (err) {
                res.status(500).send({ error: "Failed to get movie with id " + id });
            }
        });
    }
    webSocket() {
        this.app.ws('/ws', (ws, req) => {
            logger_1.logger.info('Client attempting WS connection');
            const token = typeof req.query.token === 'string' ? req.query.token : '';
            if (!token) {
                logger_1.logger.error('Missing JWT token in WS connection');
                ws.send(JSON.stringify({ type: 'error', message: 'Missing token' }));
                ws.close();
                return;
            }
            jsonwebtoken_1.default.verify(token, "d3b5c4c8539dfcfcc883b6fd63bbdb23", (err, decoded) => {
                if (err) {
                    logger_1.logger.error('Invalid or expired token');
                    ws.send(JSON.stringify({ type: 'error', message: 'Invalid or expired token' }));
                    ws.close();
                    return;
                }
                const user = decoded;
                ws.user = user;
                logger_1.logger.info(`WebSocket connected and authenticated for user ${user.id || user.userId}`);
                ws.on('message', (msg) => {
                    logger_1.logger.info(`Message from user ${user.id || user.userId}: ${msg}`);
                });
                ws.on('close', () => {
                    logger_1.logger.info(`WebSocket closed for user ${user.id || user.userId}`);
                });
            });
        });
    }
    login() {
        this.app.post("/login", async (req, res) => {
            const { username, password } = req.body;
            logger_1.logger.debug(req.body);
            logger_1.logger.info(`A login request with username= ${username} and passoword= ${password}`);
            try {
                const token = await this.movieRepository.login(username, password);
                if (token == "") {
                    return res.status(401).json({ error: "No user found!" });
                }
                else {
                    return res.status(200).json({ token: token });
                }
            }
            catch (error) {
                return res.status(500).json({ error: "Failed to login!" });
            }
        });
    }
    addMovie() {
        this.app.post("/", jwt_1.authenticateJWT, async (req, res) => {
            let movie = req.body;
            try {
                logger_1.logger.info("Adding a new resource");
                const user = req.user;
                const userId = user.id || user.userId;
                if (!userId) {
                    return res.status(401).send({ error: "User ID missing in token" });
                }
                movie.owner_id = userId;
                const added = await this.movieRepository.addMovie(movie);
                this.appW.getWss().clients.forEach((client) => {
                    logger_1.logger.debug("Starting to send notifications");
                    if (client.readyState === ws_1.WebSocket.OPEN) {
                        const clientUser = client.user;
                        logger_1.logger.debug(clientUser.id);
                        if (clientUser && clientUser.id === userId) {
                            client.send(JSON.stringify(added));
                            logger_1.logger.info(`Sent new movie to user ${userId}: ${JSON.stringify(added)}`);
                        }
                    }
                });
                res.send(added);
            }
            catch (error) {
                console.error(error);
                res.status(500).send({ error: "Failed to add movie to the user!" });
            }
        });
    }
    updateMovie() {
        this.app.put("/movies/:id", jwt_1.authenticateJWT, async (req, res) => {
            try {
                const user = req.user;
                const userId = user.id || user.userId;
                if (!userId) {
                    return res.status(401).send({ error: "User ID missing in token" });
                }
                const movie = req.body;
                movie.id = Number(req.params.id);
                movie.owner_id = userId;
                const updated = await this.movieRepository.updateMovie(movie);
                res.send(updated);
            }
            catch (error) {
                logger_1.logger.error(error);
                res.status(500).send({ error: "Failed to update movie!" });
            }
        });
    }
    uploadPhoto() {
        this.app.post("/upload", jwt_1.authenticateJWT, async (req, res) => {
            const { data, fileName } = req.body;
            if (!data) {
                return res.status(400).send({ error: "Missing photo data" });
            }
            try {
                const base64Data = data.includes("base64,") ? data.split("base64,")[1] : data;
                const buffer = Buffer.from(base64Data, "base64");
                const safeName = fileName || `movie_${Date.now()}.jpeg`;
                const targetPath = path_1.default.join(this.uploadDir, safeName);
                await fs_1.default.promises.writeFile(targetPath, buffer);
                const url = `${req.protocol}://${req.get("host")}/uploads/${safeName}`;
                res.send({ photoUrl: url, photoPath: safeName });
            }
            catch (error) {
                logger_1.logger.error(error);
                res.status(500).send({ error: "Failed to upload photo" });
            }
        });
    }
}
exports.RestService = RestService;
