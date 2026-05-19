package com.example.moviesapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.data.remote.omdb.OmdbApi
import com.example.moviesapp.data.remote.omdb.toDomain
import com.example.moviesapp.domain.model.Movie

class OmdbPagingSource(
    private val api: OmdbApi,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = api.searchMovies(
                query = query.ifBlank { "star" },
                page = page
            )

            if (response.response == "False" || response.search == null) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = null
                )
            }

            val movies = response.search.map { it.toDomain() }

            val totalResults = response.totalResults?.toIntOrNull()
            val pageSize = 10

            val totalPages = if (totalResults != null && totalResults > 0) {
                (totalResults + pageSize - 1) / pageSize
            } else null

            val nextKey = if (totalPages != null && page >= totalPages) null else page + 1

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPos = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPos) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}