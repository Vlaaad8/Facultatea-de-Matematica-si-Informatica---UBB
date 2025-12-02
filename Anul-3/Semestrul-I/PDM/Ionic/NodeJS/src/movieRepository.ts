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
            
            // Convert DECIMAL values to numbers for latitude and longitude
            const movies = data as Movie[];
            movies.forEach(movie => {
                if (movie.latitude != null) {
                    movie.latitude = Number(movie.latitude);
                }
                if (movie.longitude != null) {
                    movie.longitude = Number(movie.longitude);
                }
            });
            console.log("Am trimis catre client un number de " +movies.length)
            return movies;
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
            const movie = rows[0];
            
            // Convert DECIMAL values to numbers for latitude and longitude
            if (movie) {
                if (movie.latitude != null) {
                    movie.latitude = Number(movie.latitude);
                }
                if (movie.longitude != null) {
                    movie.longitude = Number(movie.longitude);
                }
            }
            
            return movie;

        } catch (err) {
            console.log(err);
            throw err;
        } finally {
            if (connection) {
                connection.release();
            }
        }
    }

    private formatPremierDate(value: Date | string): string {
        if (value instanceof Date) {
            return value.toISOString().slice(0, 19).replace('T', ' ');
        }
        // If it's a string, try to parse it as Date first
        if (typeof value === 'string') {
            const date = new Date(value);
            if (!isNaN(date.getTime())) {
                return date.toISOString().slice(0, 19).replace('T', ' ');
            }
            // If it's already in the correct format, return as is
            return value;
        }
        // Fallback: use current date
        return new Date().toISOString().slice(0, 19).replace('T', ' ');
    }

    public async addMovie(movie: Movie): Promise<Movie> {
        let connection;
        try {
            connection = await this.pool.getConnection();
            logger.debug(`Adding a new resource ${movie.name} ${movie.running} ${movie.premierDate} ${movie.rating}`);

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

    public async updateMovie(movie: Movie): Promise<Movie> {
        let connection;
        try {
            connection = await this.pool.getConnection();
            logger.info(`Updating movie ${movie.id}`);
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