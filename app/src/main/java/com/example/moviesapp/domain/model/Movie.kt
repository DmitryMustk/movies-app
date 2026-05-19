package com.example.moviesapp.domain.model

data class Movie(
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String?,
    val type: String
)

data class MovieDetails(
    val imdbId: String,
    val title: String,
    val year: String,
    val posterUrl: String?,
    val type: String,
    val plot: String?,
    val genre: String?,
    val director: String?,
    val runtime: String?,
    val imdbRating: String?
)
