package com.example.moviesapp.presentation.movies.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.moviesapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesListScreen(
    state: MoviesListState,
    pagingItems: Flow<PagingData<Movie>>,
    onMovieClick: (Movie) -> Unit,
    onPullToRefresh: () -> Unit,
    onRetry: () -> Unit,
    onQueryChange: (String) -> Unit
) {
    val lazyPagingItems = pagingItems.collectAsLazyPagingItems()

    val isRefreshing =
        state.isRefreshing || lazyPagingItems.loadState.refresh is LoadState.Loading

    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            onPullToRefresh()
            lazyPagingItems.refresh()
        },
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                label = { Text("Поиск по названию") },
                singleLine = true
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = lazyPagingItems.itemCount,
                    key = { index ->
                        val item = lazyPagingItems[index]
                        item?.imdbId ?: index
                    }
                ) { index ->
                    val movie = lazyPagingItems[index]
                    if (movie != null) {
                        MovieListItem(
                            movie = movie,
                            onClick = { onMovieClick(movie) }
                        )
                        Divider()
                    }
                }

                item {
                    when (val appendState = lazyPagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is LoadState.Error -> {
                            TextButton(
                                onClick = { lazyPagingItems.retry() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Ошибка загрузки. Нажмите, чтобы повторить")
                            }
                        }

                        else -> Unit
                    }
                }

                if (lazyPagingItems.loadState.refresh is LoadState.Error) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Не удалось загрузить фильмы")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = {
                                onRetry()
                                lazyPagingItems.retry()
                            }) {
                                Text("Повторить")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        if (movie.posterUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(movie.posterUrl),
                contentDescription = movie.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
        } else {
            Spacer(modifier = Modifier.width(4.dp))
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = movie.year,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = movie.type,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
