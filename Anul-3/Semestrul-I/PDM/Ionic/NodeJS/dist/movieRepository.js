"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MovieRepository = void 0;
const mariadb_1 = __importDefault(require("mariadb"));
const logger_1 = require("./logger");
const jsonwebtoken_1 = __importDefault(require("jsonwebtoken"));
class MovieRepository {
    constructor() {
        this.pool = mariadb_1.default.createPool({
            host: "localhost",
            user: "root",
            password: "",
            database: "pdm",
            connectionLimit: 5
        });
        this.SECRET_KEY = "d3b5c4c8539dfcfcc883b6fd63bbdb23";
    }
    async getMovies(userId, pageNumber, pageSize) {
        let connection;
        logger_1.logger.info('Entered getMovies method');
        try {
            connection = await this.pool.getConnection();
            let offset = pageNumber * pageSize;
            logger_1.logger.info("Size: " + offset + " " + pageNumber);
            let data = await connection.query("SELECT * FROM movies WHERE owner_id = ? LIMIT ? OFFSET ?", [userId, pageSize, offset]);
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
    formatPremierDate(value) {
        if (value instanceof Date) {
            return value.toISOString().slice(0, 19).replace('T', ' ');
        }
        // assume already ok
        return value;
    }
    async addMovie(movie) {
        let connection;
        try {
            connection = await this.pool.getConnection();
            logger_1.logger.debug(`Adding a new resource ${movie.name} ${movie.running} ${movie.premierDate} ${movie.rating}`);
            const premierDateString = this.formatPremierDate(movie.premierDate);
            const runningValue = movie.running ? 1 : 0;
            const sql = "INSERT INTO movies(name, premierDate, rating, running, owner_id, photoPath, photoUrl, latitude, longitude, locationLabel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            const result = await connection.query(sql, [
                movie.name,
                premierDateString,
                movie.rating,
                runningValue,
                movie.owner_id,
                movie.photoPath ?? null,
                movie.photoUrl ?? null,
                movie.latitude ?? null,
                movie.longitude ?? null,
                movie.locationLabel ?? null
            ]);
            const insertedId = result.insertId;
            movie.id = Number(insertedId);
            logger_1.logger.info(`Added movie with id ${insertedId}`);
            return movie;
        }
        catch (error) {
            throw error;
        }
        finally {
            if (connection) {
                connection.release();
            }
        }
    }
    async updateMovie(movie) {
        let connection;
        try {
            connection = await this.pool.getConnection();
            logger_1.logger.info(`Updating movie ${movie.id}`);
            const premierDateString = this.formatPremierDate(movie.premierDate);
            const runningValue = movie.running ? 1 : 0;
            const sql = `UPDATE movies 
                         SET name = ?, premierDate = ?, rating = ?, running = ?, photoPath = ?, photoUrl = ?, latitude = ?, longitude = ?, locationLabel = ?
                         WHERE id = ? AND owner_id = ?`;
            await connection.query(sql, [
                movie.name,
                premierDateString,
                movie.rating,
                runningValue,
                movie.photoPath ?? null,
                movie.photoUrl ?? null,
                movie.latitude ?? null,
                movie.longitude ?? null,
                movie.locationLabel ?? null,
                movie.id,
                movie.owner_id
            ]);
            return movie;
        }
        catch (error) {
            throw error;
        }
        finally {
            if (connection) {
                connection.release();
            }
        }
    }
    async login(username, password) {
        let connection;
        try {
            connection = await this.pool.getConnection();
            const rows = await connection.query("SELECT * FROM users WHERE username = ?", [username]);
            const foundUser = rows[0];
            if (!foundUser) {
                return "";
            }
            if (foundUser.password !== password) {
                throw new Error("Invalid credentials");
            }
            return jsonwebtoken_1.default.sign({ id: foundUser.id, username: foundUser.username }, this.SECRET_KEY, { expiresIn: "1h" });
        }
        catch (err) {
            throw err;
        }
        finally {
            if (connection)
                connection.release();
        }
    }
}
exports.MovieRepository = MovieRepository;
