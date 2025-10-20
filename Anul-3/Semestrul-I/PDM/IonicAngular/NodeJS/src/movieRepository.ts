import mariadb from "mariadb";
import {Movie} from "./model/movie"
import {logger} from "./logger";

export class MovieRepository {

    private pool = mariadb.createPool({
        host: "localhost",
        user: "root",
        password: "",
        database: "pdm",
        connectionLimit: 5
    });

    public async getMovies(pageNumber: number, pageSize: number): Promise<Movie[]> {
        let connection;
        try {
            connection = await this.pool.getConnection();
            let offset = (pageNumber) * pageSize;
            let data = await connection.query("SELECT * FROM movies LIMIT ? OFFSET ?", [pageSize, offset]);
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
            const rows=  await connection.query("SELECT * FROM movies where id = ?", [id]) ;
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
    public async getNumberOfEntries(): Promise<number> {
        let connection;
        try{
            connection = await this.pool.getConnection();
            return await connection.query("SELECT COUNT(*) FROM movies")
        }
        catch(err){
            console.log(err);
            throw err;
        }
        finally{
            if (connection) {
                connection.release();
            }
        }
    }
}