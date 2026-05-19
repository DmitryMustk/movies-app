package com.example.moviesapp.presentation.movies.list

data class MoviesListState(
    val query: String = "star",
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)
