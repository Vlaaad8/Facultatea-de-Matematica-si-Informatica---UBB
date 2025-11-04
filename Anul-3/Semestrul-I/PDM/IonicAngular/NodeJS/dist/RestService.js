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
class RestService {
    constructor() {
        this.port = 8080;
        this.movieRepository = new movieRepository_1.MovieRepository();
        this.app = (0, express_1.default)();
        this.app.use((0, cors_1.default)());
        this.appW = (0, express_ws_1.default)(this.app);
        this.sayHi();
        this.getMovies();
        this.getMovie();
        this.webSocket();
        this.app.listen(this.port, () => {
            logger_1.logger.log(`Server started on port ${this.port}`);
        });
    }
    sayHi() {
        this.app.get("/", (req, res) => {
            res.send("Hi");
        });
    }
    getMovies() {
        this.app.get("/movies", async (req, res) => {
            try {
                let pageNumber = parseInt(req.query.pageNumber);
                let pageSize = parseInt(req.query.pageSize);
                logger_1.logger.info(`A user requested movies with pageNumber=${pageNumber} and pageSize=${pageSize}`);
                const movies = await this.movieRepository.getMovies(pageNumber, pageSize);
                res.send(movies);
            }
            catch (err) {
                res.status(500).send({ error: "Failed to get movies! Check the API or server!" });
            }
        });
    }
    getMovie() {
        this.app.get(`/movies/:id`, async (req, res) => {
            let id = parseInt(req.params.id);
            logger_1.logger.info(`A request came to get movie with id=${id}`);
            try {
                const movie = await this.movieRepository.getMovie(id);
                if (movie) {
                    this.appW.getWss().clients.forEach((client) => {
                        if (client.readyState === ws_1.WebSocket.OPEN) {
                            client.send(JSON.stringify({ title: 'New interested client!', message: movie.name }));
                        }
                    });
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
        this.app.ws('/ws', (ws) => {
            logger_1.logger.info('Client connected');
            ws.on('message', (msg) => {
                logger_1.logger.info('Received:', msg.toString());
            });
            ws.send(JSON.stringify({ type: 'info', message: 'Welcome!' }));
        });
    }
}
exports.RestService = RestService;
