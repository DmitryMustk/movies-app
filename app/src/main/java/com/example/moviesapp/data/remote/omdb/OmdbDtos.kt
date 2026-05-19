package com.example.moviesapp.data.remote.omdb

import com.google.gson.annotations.SerializedName

data class OmdbSearchResponseDto(
    @SerializedName("Search")
    val search: List<OmdbMovieShortDto>? = null,
    @SerializedName("totalResults")
    val totalResults: String? = null,
    @SerializedName("Response")
    val response: String? = null,
    @SerializedName("Error")
    val error: String? = null
)

data class OmdbMovieShortDto(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("imdbID")
    val imdbId: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Poster")
    val posterUrl: String?
)

data class OmdbMovieDetailsDto(
    @SerializedName("Title")
    val title: String?,
    @SerializedName("Year")
    val year: String?,
    @SerializedName("imdbID")
    val imdbId: String?,
    @SerializedName("Type")
    val type: String?,
    @SerializedName("Poster")
    val posterUrl: String?,
    @SerializedName("Plot")
    val plot: String?,
    @SerializedName("Genre")
    val genre: String?,
    @SerializedName("Director")
    val director: String?,
    @SerializedName("Runtime")
    val runtime: String?,
    @SerializedName("imdbRating")
    val imdbRating: String?,
    @SerializedName("Response")
    val response: String?,
    @SerializedName("Error")
    val error: String?
)
