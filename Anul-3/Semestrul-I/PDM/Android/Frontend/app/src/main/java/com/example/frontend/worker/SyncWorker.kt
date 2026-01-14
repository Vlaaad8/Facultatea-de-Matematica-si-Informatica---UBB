package com.example.frontend.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.frontend.Movie
import com.example.frontend.RetrofitClient
import com.example.frontend.data.AppDatabase
import com.example.frontend.data.MovieEntity
import com.example.frontend.model.UploadRequest
import com.example.frontend.utils.FileUtils
import java.io.File

class SyncWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val movieDao = database.movieDao()

        val unsyncedMovies = movieDao.getUnsyncedMovies()
        Log.d("SyncWorker", "Found ${unsyncedMovies.size} unsynced movies.")

        for (movieEntity in unsyncedMovies) {
            try {
                if (movieEntity.syncStatus == 1) {
                    uploadMovie(movieEntity, movieDao)
                }
            } catch (e: Exception) {
                Log.e("SyncWorker", "Error syncing movie ${movieEntity.name}", e)
                return Result.retry()
            }
        }
        return Result.success()
    }

    private suspend fun uploadMovie(entity: MovieEntity, movieDao: com.example.frontend.data.MovieDao) {
        Log.d("SyncWorker", "Processing movie: ${entity.name}")

        var serverPhotoPath: String? = null

        if (entity.imagePath != null) {
            val file = File(entity.imagePath)
            if (file.exists()) {
                val base64String = FileUtils.fileToBase64(file)
                if (base64String != null) {
                    try {
                        val uploadReq = UploadRequest(
                            data = base64String,
                            fileName = file.name
                        )
                        val uploadRes = RetrofitClient.movieService.uploadPhoto(uploadReq)
                        if (uploadRes.isSuccessful && uploadRes.body() != null) {
                            serverPhotoPath = uploadRes.body()!!.photoPath
                            Log.d("SyncWorker", "Photo uploaded via Worker! Path: $serverPhotoPath")
                        }
                    } catch (e: Exception) {
                        Log.e("SyncWorker", "Photo upload failed, proceeding without photo.", e)
                    }
                }
            }
        }
        val movieToSend = Movie(
            id = 0,
            name = entity.name,
            premierDate = entity.premierDate,
            rating = entity.rating,
            running = entity.running,
            owner_id = entity.owner_id,
            imagePath = serverPhotoPath
        )


        val response = RetrofitClient.movieService.addMovie(movieToSend)

        if (response.isSuccessful && response.body() != null) {
            val serverMovie = response.body()!!

            movieDao.deleteById(entity.id)

            val syncedEntity = MovieEntity(
                id = serverMovie.id,
                name = serverMovie.name,
                premierDate = serverMovie.premierDate,
                rating = serverMovie.rating,
                running = serverMovie.running,
                owner_id = serverMovie.owner_id,
                imagePath = serverMovie.imagePath,
                syncStatus = 0
            )
            movieDao.insert(syncedEntity)

            Log.d("SyncWorker", "Synced success: ${entity.name}")
        } else {
            Log.e("SyncWorker", "Failed to sync movie: ${response.code()}")
            throw Exception("Server error ${response.code()}")
        }
    }
}