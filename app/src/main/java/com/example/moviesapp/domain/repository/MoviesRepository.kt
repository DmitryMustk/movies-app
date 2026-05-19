package com.example.moviesapp.domain.repository

import androidx.paging.PagingData
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getMoviesPaging(query: String): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(imdbId: String): MovieDetails
}
