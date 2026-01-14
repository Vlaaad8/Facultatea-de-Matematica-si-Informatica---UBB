package com.example.frontend

import com.example.frontend.service.MovieService
import com.example.frontend.service.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val URL = "http://10.0.2.2:8081/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val token = TokenManager.token

            val requestBuilder = original.newBuilder()

            if (token != null) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
    val movieService: MovieService by lazy {
        retrofit.create(MovieService::class.java)
    }
}