package com.example.frontend

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Movie(
    val id: Int,


    var name: String,

    var premierDate: Date,
    var rating: Double,
    val running : Int,
    val owner_id: Int,

    @SerializedName("photoPath")
    var imagePath: String? = null
)