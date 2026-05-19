package com.example.moviesapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesapp.presentation.movies.details.MovieDetailsScreen
import com.example.moviesapp.presentation.movies.details.MovieDetailsViewModel
import com.example.moviesapp.presentation.movies.list.MoviesListScreen
import com.example.moviesapp.presentation.movies.list.MoviesListViewModel

@Composable
fun MoviesNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MoviesList.route
    ) {

        composable(Screen.MoviesList.route) {
            val vm: MoviesListViewModel = hiltViewModel()
            val state by vm.state

            MoviesListScreen(
                state = state,
                pagingItems = vm.pagingItems,
                onMovieClick = { movie ->
                    navController.navigate(Screen.MovieDetails.route(movie.imdbId))
                },
                onPullToRefresh = { vm.onRefresh() },
                onRetry = { vm.onRetry() },
                onQueryChange = { vm.onQueryChange(it) }
            )
        }

        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument(Screen.MovieDetails.ARG_IMDB_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            val vm: MovieDetailsViewModel = hiltViewModel()
            val uiState by vm.state

            MovieDetailsScreen(
                state = uiState,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
