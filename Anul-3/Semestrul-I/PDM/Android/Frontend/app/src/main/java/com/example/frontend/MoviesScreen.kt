package com.example.frontend

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.frontend.service.SocketManager
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(onMovieClick: (Int) -> Unit, onAddClick: () -> Unit) {
    var movies by remember { mutableStateOf(emptyList<Movie>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        SocketManager.connect()
        try {
            val response = RetrofitClient.movieService.getMovies(page = 0, size = 10)
            if (response.isSuccessful) {
                movies = response.body() ?: emptyList()
                println("Lista de filme: $movies")
            } else {
                println("Eroare server: ${response.code()}")
            }
        } catch (e: Exception) {
            println("Eroare server: ${e.message}")
        } finally {
            isLoading = false
        }
    }
    //TODO Fix this
    LaunchedEffect(Unit) {
        SocketManager.events.collect { jsonString ->
            try {
                println("DEBUG: Procesez mesajul: $jsonString")

                // AICI E SCHIMBAREA: Formatul datei trebuie să se potrivească cu ce trimite WebSocket-ul
                // "Dec 9, 2025 2:00:00 AM" -> "MMM d, yyyy h:mm:ss a"
                val gson = GsonBuilder()
                    .setDateFormat("MMM d, yyyy h:mm:ss a")
                    .create()

                val newMovie = gson.fromJson(jsonString, Movie::class.java)

                if (newMovie != null && newMovie.name.isNotEmpty()) {
                    println("DEBUG: Adaug filmul în listă: ${newMovie.name}")
                    movies = movies + newMovie
                }
            } catch (e: Exception) {
                println("EROARE PARSING: ${e.message}")
                // Dacă nu merge cu primul format, încercăm și formatul ISO ca rezervă
                try {
                    val gsonIso = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create()
                    val newMovie = gsonIso.fromJson(jsonString, Movie::class.java)
                    if (newMovie != null) movies = movies + newMovie
                } catch (e2: Exception) {
                    println("Niciun format de dată nu a funcționat.")
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Filme") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddClick() }) {
                Icon(Icons.Default.Add, contentDescription = "Adaugă Film")
            }
        }

    ) { innerPadding ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(movies) { movie ->
                        MovieRow(movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                }
            }
        }
    }
}
