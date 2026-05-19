package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import com.example.moviesapp.presentation.navigation.MoviesNavHost
import com.example.moviesapp.presentation.ui.theme.MoviesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesRoot()
        }
    }
}

@Composable
fun MoviesRoot() {
    MoviesTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            MoviesNavHost()
        }
    }
}
