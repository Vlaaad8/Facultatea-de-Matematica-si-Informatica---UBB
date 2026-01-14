package com.example.frontend.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.frontend.Movie
import java.util.Date

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val premierDate: Date,
    val rating: Double,
    val running: Int,
    val owner_id: Int,
    val imagePath: String? = null,
    val syncStatus: Int = 0
) {
    fun toMovie(): Movie {
        return Movie(
            id = id,
            name = name,
            premierDate = premierDate,
            rating = rating,
            running = running,
            owner_id = owner_id,
            imagePath = imagePath
        )
    }
}