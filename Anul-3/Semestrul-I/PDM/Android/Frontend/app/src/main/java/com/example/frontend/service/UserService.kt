package com.example.frontend.service

import com.example.frontend.LoginRequest
import com.example.frontend.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("login")
     suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}