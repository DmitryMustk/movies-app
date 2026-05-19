package com.example.moviesapp.data.remote.omdb

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int
    ): OmdbSearchResponseDto

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full"
    ): OmdbMovieDetailsDto
}
