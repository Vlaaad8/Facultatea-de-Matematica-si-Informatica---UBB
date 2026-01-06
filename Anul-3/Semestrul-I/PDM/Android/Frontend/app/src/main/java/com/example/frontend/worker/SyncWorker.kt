package com.example.frontend.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.frontend.RetrofitClient
import com.example.frontend.TokenManager
import com.example.frontend.data.AppDatabase
import com.example.frontend.data.MovieEntity
import com.example.frontend.utils.NotificationUtils

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        TokenManager.loadToken(applicationContext)
        if (TokenManager.token == null) return Result.failure()

        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.movieDao()

        try {
            val unsyncedMovies = dao.getUnsyncedMovies()

            for (movie in unsyncedMovies) {
                if (movie.syncStatus == 1) {
                    try {

                        val movieToSend = movie.toMovie().copy(id = 0)
                        val response = RetrofitClient.movieService.addMovie(movieToSend)

                        if (response.isSuccessful && response.body() != null) {
                            val serverMovie = response.body()!!


                            dao.deleteById(movie.id)


                            val newEntity = MovieEntity(
                                serverMovie.id, serverMovie.name, serverMovie.premierDate,
                                serverMovie.rating, serverMovie.running, serverMovie.owner_id,
                                syncStatus = 0
                            )
                            dao.insert(newEntity)
                            Log.d("SyncWorker", "Synced NEW movie: ${serverMovie.name}")
                        }
                    } catch (e: Exception) { Log.e("SyncWorker", "Fail add", e) }

                } else if (movie.syncStatus == 2) {

                    try {
                        val response = RetrofitClient.movieService.editMovie(movie.id, movie.toMovie())
                        if (response.isSuccessful) {

                            val syncedEntity = movie.copy(syncStatus = 0)
                            dao.insert(syncedEntity)
                            Log.d("SyncWorker", "Synced EDITED movie: ${movie.name}")
                        }
                    } catch (e: Exception) { Log.e("SyncWorker", "Fail edit", e) }
                }
            }

            if (unsyncedMovies.isNotEmpty()) {
                NotificationUtils.showNotification(applicationContext, "Upload complet", "Modificarile offline au fost salvate pe server.")
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}