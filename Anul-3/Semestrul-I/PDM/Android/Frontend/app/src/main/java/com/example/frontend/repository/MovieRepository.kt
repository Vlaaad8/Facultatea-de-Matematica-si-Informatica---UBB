package com.example.frontend.repository

import android.content.Context
import android.util.Log
import com.example.frontend.Movie
import com.example.frontend.RetrofitClient
import com.example.frontend.data.MovieDao
import com.example.frontend.data.MovieEntity
import com.example.frontend.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class MovieRepository(private val movieDao: MovieDao, private val context: Context) {

    val movies: Flow<List<Movie>> = movieDao.getAllMovies().map { entities ->
        entities.map { it.toMovie() }
    }

    suspend fun refreshMovies() {
        if (!NetworkUtils.isInternetAvailable(context)) return
        try {
            val response = RetrofitClient.movieService.getMovies(0, 100)
            if (response.isSuccessful && response.body() != null) {
                val entities = response.body()!!.map {
                    MovieEntity(it.id, it.name, it.premierDate, it.rating, it.running, it.owner_id, syncStatus = 0)
                }
                movieDao.insertAll(entities)
            }
        } catch (e: Exception) { Log.e("Repo", "Error refresh", e) }
    }

    // --- MODIFICARE MAJORĂ AICI ---
    suspend fun addMovie(movie: Movie) {
        if (NetworkUtils.isInternetAvailable(context)) {
            try {
                Log.d("Repo", "Avem net, incercam serverul...")
                val response = RetrofitClient.movieService.addMovie(movie)

                if (response.isSuccessful && response.body() != null) {
                    val serverMovie = response.body()!!
                    val entity = MovieEntity(
                        serverMovie.id, serverMovie.name, serverMovie.premierDate,
                        serverMovie.rating, serverMovie.running, serverMovie.owner_id,
                        syncStatus = 0
                    )
                    movieDao.insert(entity)
                    return // Ieșim din funcție, totul e gata
                }
            } catch (e: Exception) {
                Log.e("Repo", "Eroare server: ${e.message}")
            }
        }

        // Fallback Offline
        Log.d("Repo", "Salvam OFFLINE...")
        val tempId = if (movie.id == 0) Random.nextInt(100000, 999999) else movie.id

        val offlineEntity = MovieEntity(
            id = tempId,
            name = movie.name,
            premierDate = movie.premierDate,
            rating = movie.rating,
            running = movie.running,
            owner_id = movie.owner_id,
            syncStatus = 1
        )
        movieDao.insert(offlineEntity)
    }

    // SCHIMBARE: Am scos ": Boolean"
    suspend fun editMovie(movieId: Int, movie: Movie) {
        if (NetworkUtils.isInternetAvailable(context)) {
            try {
                val response = RetrofitClient.movieService.editMovie(movieId, movie)
                if (response.isSuccessful) {
                    val entity = MovieEntity(
                        movieId, movie.name, movie.premierDate,
                        movie.rating, movie.running, movie.owner_id,
                        syncStatus = 0
                    )
                    movieDao.insert(entity)
                    return // Ieșim, gata
                }
            } catch (e: Exception) {
                Log.e("Repo", "Eroare editare server: ${e.message}")
            }
        }

        // Fallback Offline
        Log.d("Repo", "Editare OFFLINE...")
        val offlineEntity = MovieEntity(
            id = movieId,
            name = movie.name,
            premierDate = movie.premierDate,
            rating = movie.rating,
            running = movie.running,
            owner_id = movie.owner_id,
            syncStatus = 2
        )
        movieDao.insert(offlineEntity)
    }
}