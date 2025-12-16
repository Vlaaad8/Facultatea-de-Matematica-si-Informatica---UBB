package com.example.frontend.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
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

    // 0 = Synced, 1 = Created Offline, 2 = Edited Offline
    val syncStatus: Int = 0
) {
    fun toMovie(): Movie {
        return Movie(
            id = id,
            name = name,
            premierDate = premierDate,
            rating = rating,
            running = running,
            owner_id = owner_id
        )
    }
}

// Convertorul Date rămâne la fel...
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}