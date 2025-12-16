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
            // 1. Luăm filmele care așteaptă sincronizarea
            val unsyncedMovies = dao.getUnsyncedMovies()
            Log.d("SyncWorker", "Found ${unsyncedMovies.size} unsynced movies")

            for (movie in unsyncedMovies) {
                if (movie.syncStatus == 1) {
                    // --- CASE 1: CREATED OFFLINE (POST) ---
                    try {
                        // Construim obiectul Movie (fără ID, sau cu ID 0 pt server)
                        val movieToSend = movie.toMovie().copy(id = 0)
                        val response = RetrofitClient.movieService.addMovie(movieToSend)

                        if (response.isSuccessful && response.body() != null) {
                            val serverMovie = response.body()!!

                            // Ștergem filmul temporar (cel cu ID random)
                            dao.deleteById(movie.id)

                            // Inserăm filmul real primit de la server (cu ID corect)
                            val newEntity = MovieEntity(
                                serverMovie.id, serverMovie.name, serverMovie.premierDate,
                                serverMovie.rating, serverMovie.running, serverMovie.owner_id,
                                syncStatus = 0 // Clean
                            )
                            dao.insert(newEntity)
                            Log.d("SyncWorker", "Synced NEW movie: ${serverMovie.name}")
                        }
                    } catch (e: Exception) { Log.e("SyncWorker", "Fail add", e) }

                } else if (movie.syncStatus == 2) {
                    // --- CASE 2: EDITED OFFLINE (PUT) ---
                    try {
                        val response = RetrofitClient.movieService.editMovie(movie.id, movie.toMovie())
                        if (response.isSuccessful) {
                            // Updatăm statusul la 0 (Synced)
                            val syncedEntity = movie.copy(syncStatus = 0)
                            dao.insert(syncedEntity)
                            Log.d("SyncWorker", "Synced EDITED movie: ${movie.name}")
                        }
                    } catch (e: Exception) { Log.e("SyncWorker", "Fail edit", e) }
                }
            }

            if (unsyncedMovies.isNotEmpty()) {
                NotificationUtils.showNotification(applicationContext, "Upload complet", "Modificările offline au fost salvate pe server.")
            }

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}