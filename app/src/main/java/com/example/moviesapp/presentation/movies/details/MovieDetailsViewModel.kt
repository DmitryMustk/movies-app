package com.example.moviesapp.presentation.movies.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.domain.model.MovieDetails
import com.example.moviesapp.domain.repository.MoviesRepository
import com.example.moviesapp.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailsState(
    val isLoading: Boolean = true,
    val data: MovieDetails? = null,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isFavoriteLoading: Boolean = false
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imdbId: String =
        savedStateHandle[Screen.MovieDetails.ARG_IMDB_ID] ?: ""

    private val _state = mutableStateOf(MovieDetailsState())
    val state = _state

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )
            try {
                val details = repository.getMovieDetails(imdbId)

                _state.value = _state.value.copy(
                    isLoading = false,
                    data = details,
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка загрузки"
                )
            }
        }
    }
}
