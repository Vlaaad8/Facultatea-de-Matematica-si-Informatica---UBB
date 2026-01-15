package com.example.frontend.model

data class UploadRequest(
    val data: String,
    val fileName: String
)

data class UploadResponse(
    val photoUrl: String,
    val photoPath: String
)