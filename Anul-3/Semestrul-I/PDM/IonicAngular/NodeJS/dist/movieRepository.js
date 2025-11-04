"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MovieRepository = void 0;
const mariadb_1 = __importDefault(require("mariadb"));
const logger_1 = require("./logger");
class MovieRepository {
    constructor() {
        this.pool = mariadb_1.default.createPool({
            host: "localhost",
            user: "root",
            password: "",
            database: "pdm",
            connectionLimit: 5
        });
    }
    async getMovies(pageNumber, pageSize) {
        let connection;
        try {
            connection = await this.pool.getConnection();
            let offset = (pageNumber) * pageSize;
            let data = await connection.query("SELECT * FROM movies LIMIT ? OFFSET ?", [pageSize, offset]);
            return data;
        }
        catch (err) {
            logger_1.logger.error(err);
            throw err;
        }
        finally {
            if (connection) {
                connection.release();
            }
        }
    }
    async getMovie(id) {
        let connection;
        try {
            connection = await this.pool.getConnection();
            const rows = await connection.query("SELECT * FROM movies where id = ?", [id]);
            return rows[0];
        }
        catch (err) {
            console.log(err);
            throw err;
        }
        finally {
            if (connection) {
                connection.release();
            }
        }
    }
    async getNumberOfEntries() {
        let connection;
        try {
            connection = await this.pool.getConnection();
            return await connection.query("SELECT COUNT(*) FROM movies");
        }
        catch (err) {
            console.log(err);
            throw err;
        }
        finally {
            if (connection) {
                connection.release();
            }
        }
    }
}
exports.MovieRepository = MovieRepository;
