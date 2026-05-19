package com.example.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.data.remote.omdb.OmdbApi
import com.example.moviesapp.data.remote.omdb.toDomain
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.domain.model.MovieDetails
import com.example.moviesapp.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val omdbApi: OmdbApi,
) : MoviesRepository {

    override fun getMoviesPaging(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { OmdbPagingSource(omdbApi, query) }
        ).flow
    }

    override suspend fun getMovieDetails(imdbId: String): MovieDetails {
        val dto = omdbApi.getMovieDetails(imdbId)
        if (dto.response == "False") {
            throw IllegalStateException(dto.error ?: "Unknown error")
        }
        return dto.toDomain()
    }
}
