package com.example.frontend

import java.util.Date

data class Movie(
    val id: Int,
    var name: String,
    var premierDate: Date,
    var rating: Double,
    val running : Int,
    val owner_id: Int
)
