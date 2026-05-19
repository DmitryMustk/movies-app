package com.example.moviesapp.data.remote.omdb

import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.domain.model.MovieDetails

fun OmdbMovieShortDto.toDomain(): Movie =
    Movie(
        imdbId = imdbId,
        title = title,
        year = year,
        posterUrl = posterUrl.takeUnless { it == "N/A" },
        type = type
    )

fun OmdbMovieDetailsDto.toDomain(): MovieDetails =
    MovieDetails(
        imdbId = imdbId.orEmpty(),
        title = title.orEmpty(),
        year = year.orEmpty(),
        posterUrl = posterUrl.takeUnless { it == "N/A" },
        type = type.orEmpty(),
        plot = plot,
        genre = genre,
        director = director,
        runtime = runtime,
        imdbRating = imdbRating
    )
