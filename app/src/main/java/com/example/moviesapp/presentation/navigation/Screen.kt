package com.example.moviesapp.presentation.navigation

sealed class Screen(val route: String) {
    data object MoviesList : Screen("movies_list")

    data object MovieDetails : Screen("movie_details/{imdbId}") {
        const val ARG_IMDB_ID = "imdbId"
        fun route(imdbId: String) = "movie_details/$imdbId"
    }
}
