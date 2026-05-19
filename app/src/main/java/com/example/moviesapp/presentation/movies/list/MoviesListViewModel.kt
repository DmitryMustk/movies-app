package com.example.moviesapp.presentation.movies.list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesapp.domain.model.Movie
import com.example.moviesapp.domain.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _state = mutableStateOf(MoviesListState())
    val state = _state

    private val queryFlow = MutableStateFlow(_state.value.query)

    val moviesFlow: Flow<PagingData<Movie>> =
        queryFlow
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { q ->
                repository.getMoviesPaging(q)
            }
            .cachedIn(viewModelScope)

    val pagingItems by lazy {
        moviesFlow
    }

    fun onQueryChange(newQuery: String) {
        _state.value = _state.value.copy(query = newQuery)
        queryFlow.value = newQuery.ifBlank { "star" }
    }

    fun onRefresh() {
        _state.value = _state.value.copy(isRefreshing = true)
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _state.value = _state.value.copy(isRefreshing = false)
        }
    }

    fun onRetry() {
        queryFlow.value = _state.value.query.ifBlank { "star" }
    }
}
