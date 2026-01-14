package com.example.frontend.repository

import android.content.Context
import android.util.Log
import com.example.frontend.Movie
import com.example.frontend.RetrofitClient
import com.example.frontend.data.MovieDao
import com.example.frontend.data.MovieEntity
import com.example.frontend.model.UploadRequest
import com.example.frontend.utils.FileUtils
import com.example.frontend.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File // <--- Import esențial
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
                    MovieEntity(
                        id = it.id,
                        name = it.name,
                        premierDate = it.premierDate,
                        rating = it.rating,
                        running = it.running,
                        owner_id = it.owner_id,
                        imagePath = it.imagePath,
                        syncStatus = 0
                    )
                }
                movieDao.insertAll(entities)
            }
        } catch (e: Exception) { Log.e("Repo", "Error refresh", e) }
    }

    suspend fun addMovie(movie: Movie, imageFile: File?) {

        if (NetworkUtils.isInternetAvailable(context)) {
            try {
                Log.d("Repo", "Online: Preparing to add movie...")


                var serverPhotoPath: String? = null

                if (imageFile != null && imageFile.exists()) {
                    val base64String = FileUtils.fileToBase64(imageFile)
                    if (base64String != null) {
                        Log.d("Repo", "Uploading photo base64...")
                        val uploadReq = UploadRequest(
                            data = base64String,
                            fileName = imageFile.name
                        )
                        val uploadRes = RetrofitClient.movieService.uploadPhoto(uploadReq)

                        if (uploadRes.isSuccessful && uploadRes.body() != null) {
                            serverPhotoPath = uploadRes.body()!!.photoPath
                            Log.d("Repo", "Photo uploaded! Path: $serverPhotoPath")
                        } else {
                            Log.e("Repo", "Upload failed: ${uploadRes.code()}")
                        }
                    }
                }


                movie.imagePath = serverPhotoPath


                val response = RetrofitClient.movieService.addMovie(movie)

                if (response.isSuccessful && response.body() != null) {
                    val serverMovie = response.body()!!


                    val entity = MovieEntity(
                        id = serverMovie.id,
                        name = serverMovie.name,
                        premierDate = serverMovie.premierDate,
                        rating = serverMovie.rating,
                        running = serverMovie.running,
                        owner_id = serverMovie.owner_id,
                        imagePath = serverMovie.imagePath,
                        syncStatus = 0
                    )
                    movieDao.insert(entity)
                    return
                }
            } catch (e: Exception) {
                Log.e("Repo", "Eroare server: ${e.message}")
            }
        }


        Log.d("Repo", "Offline mode (or server fail). Saving locally.")
        val tempId = if (movie.id == 0) Random.nextInt(100000, 999999) else movie.id

        val localPath = imageFile?.absolutePath

        val offlineEntity = MovieEntity(
            id = tempId,
            name = movie.name,
            premierDate = movie.premierDate,
            rating = movie.rating,
            running = movie.running,
            owner_id = movie.owner_id,
            imagePath = localPath,
            syncStatus = 1
        )
        movieDao.insert(offlineEntity)
    }
    suspend fun editMovie(movieId: Int, movie: Movie) {

        if (NetworkUtils.isInternetAvailable(context)) {
            try {
                val response = RetrofitClient.movieService.editMovie(movieId, movie)
                if (response.isSuccessful && response.body() != null) {
                    val serverMovie = response.body()!!
                    val entity = MovieEntity(
                        movieId,
                        movie.name,
                        movie.premierDate,
                        movie.rating,
                        movie.running,
                        movie.owner_id,
                        imagePath = serverMovie.imagePath,
                        syncStatus = 0
                    )
                    movieDao.insert(entity)
                    return
                }
            } catch (e: Exception) {
                Log.e("Repo", "Eroare editare server: ${e.message}")
            }
        }

        Log.d("Repo", "Editare OFFLINE...")

        val offlineEntity = MovieEntity(
            id = movieId,
            name = movie.name,
            premierDate = movie.premierDate,
            rating = movie.rating,
            running = movie.running,
            owner_id = movie.owner_id,
            imagePath = movie.imagePath,
            syncStatus = 2
        )
        movieDao.insert(offlineEntity)
    }
}