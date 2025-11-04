import mariadb from "mariadb";
import {Movie} from "./model/movie"
import {logger} from "./logger";
import jwt from "jsonwebtoken";

export class MovieRepository {

    private pool = mariadb.createPool({
        host: "localhost",
        user: "root",
        password: "",
        database: "pdm",
        connectionLimit: 5
    });
    private SECRET_KEY = "d3b5c4c8539dfcfcc883b6fd63bbdb23";

    public async getMovies(userId: number, pageNumber: number, pageSize: number): Promise<Movie[]> {
        let connection;
        logger.info('Entered getMovies method')
        try {
            connection = await this.pool.getConnection();
            let offset = pageNumber * pageSize;
            logger.info("Size: " +offset + " " + pageNumber);
            let data = await connection.query("SELECT * FROM movies WHERE owner_id = ? LIMIT ? OFFSET ?", [userId,pageSize, offset]);
            return data as Movie[];
        } catch (err) {
            logger.error(err);
            throw err;
        } finally {
            if (connection) {
                connection.release();
            }
        }
    }

    public async getMovie(id: number): Promise<Movie> {

        let connection;
        try {
            connection = await this.pool.getConnection();
            const rows = await connection.query("SELECT * FROM movies where id = ?", [id]);
            return rows[0];

        } catch (err) {
            console.log(err);
            throw err;
        } finally {
            if (connection) {
                connection.release();
            }
        }
    }

    public async addMovie(movie: Movie): Promise<Movie> {
        let connection;
        try {
            connection = await this.pool.getConnection();
            logger.debug(`Adding a new resource ${movie.name} ${movie.running} ${movie.premierDate} ${movie.rating}`);

            // Format premierDate correctly
            const premierDateString = movie.premierDate instanceof Date
                ? movie.premierDate.toISOString().slice(0, 19).replace('T', ' ')
                : movie.premierDate;

            const runningValue = movie.running ? 1 : 0;

            const sql = "INSERT INTO movies(name, premierDate, rating, running, owner_id) VALUES (?, ?, ?, ?, ?)";
            const result = await connection.query(sql, [
                movie.name,
                premierDateString,
                movie.rating,
                runningValue,
                movie.owner_id
            ]);

            // Convert BigInt ID to number to avoid JSON serialization issues
            const insertedId = (result as any).insertId;
            movie.id = Number(insertedId);

            logger.info(`Added movie with id ${insertedId}`);
            return movie;

        } catch (error) {
            throw error;
        } finally {
            if (connection) {
                connection.release();
            }
        }
    }



    public async login(username: string, password: string): Promise<string> {
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

            return jwt.sign({id: foundUser.id, username: foundUser.username}, this.SECRET_KEY, {expiresIn: "1h"});
        } catch (err) {
            throw err;
        } finally {
            if (connection) connection.release();
        }
    }

}