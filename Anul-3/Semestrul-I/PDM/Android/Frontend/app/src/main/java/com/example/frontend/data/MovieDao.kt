package com.example.frontend.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>
    // --- METODA CARE ÎȚI LIPSEA ---
    // Folosită în MovieEditScreen pentru a încărca datele
    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    // --- METODE NOI PENTRU SYNC ---

    // Luăm filmele care au status 1 (Add) sau 2 (Edit)
    @Query("SELECT * FROM movies WHERE syncStatus != 0")
    suspend fun getUnsyncedMovies(): List<MovieEntity>

    // Pentru a șterge filmul temporar creat offline după ce l-am urcat pe server
    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteById(id: Int)
}