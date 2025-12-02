package com.example.frontend.service

import com.example.frontend.Movie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movies")
    suspend fun getMovies(
        @Query("pageNumber") page: Int,
        @Query("pageSize") size: Int
    ): Response<List<Movie>>

    @GET("movies/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: Int): Response<Movie>

    @PUT("movies/{movieId}")
    suspend fun editMovie(
        @Path("movieId") id: Int,
        @Body movie: Movie
    ): Response<Movie>

    @POST("/")
    suspend fun addMovie(@Body movie: Movie): Response<Movie>


}