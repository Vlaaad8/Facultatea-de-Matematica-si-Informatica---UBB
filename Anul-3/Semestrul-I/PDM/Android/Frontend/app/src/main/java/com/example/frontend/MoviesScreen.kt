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


    LaunchedEffect(true) {

        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            SocketManager.connect()
            try {
                val response = RetrofitClient.movieService.getMovies(page = 0, size = 100)
                if (response.isSuccessful) {

                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                        movies = response.body() ?: emptyList()
                        println("Lista de filme: $movies")
                    }
                } else {
                    println("Eroare server: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Eroare server: ${e.message}")
            } finally {

                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }


    LaunchedEffect(key1 = true) {
        SocketManager.events.collect { jsonString ->
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                println("DEBUG: Am primit un nou eveniment de la WebSocket: $jsonString")
                try {

                    val idMatch = "\"id\":(\\d+)".toRegex().find(jsonString)
                    val nameMatch = "\"name\":\"([^\"]+)\"".toRegex().find(jsonString)
                    val ownerMatch = "\"owner_id\":(\\d+)".toRegex().find(jsonString)
                    val ratingMatch = "\"rating\":(\\d+)".toRegex().find(jsonString)
                    val runningMatch = "\"running\":(\\d+)".toRegex().find(jsonString)

                    val id = idMatch?.groupValues?.get(1)?.toIntOrNull()
                    val name = nameMatch?.groupValues?.get(1)
                    val ownerId = ownerMatch?.groupValues?.get(1)?.toIntOrNull()
                    val rating = ratingMatch?.groupValues?.get(1)?.toDoubleOrNull()

                    val isRunning = runningMatch?.groupValues?.get(1)?.toIntOrNull() == 1

                    if (id != null && name != null && ownerId != null && rating != null) {
                        val newMovie = Movie(
                            id = id,
                            name = name,
                            owner_id = ownerId,
                            premierDate = java.util.Date(),
                            rating = rating,
                            running = 1
                        )

                        println("DEBUG: Obiectul Movie creat manual: $newMovie")

                        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                            val movieExists = movies.any { it.id == newMovie.id }
                            if (!movieExists) {
                                println("DEBUG: Adaug filmul în listă: ${newMovie.name}")
                                movies = movies + newMovie
                            } else {
                                println("DEBUG: Filmul '${newMovie.name}' (ID: ${newMovie.id}) deja există.")
                            }
                        }
                    } else {
                        println("EROARE PARSARE MANUALĂ: Nu s-au putut extrage toate datele.")
                    }
                } catch (e: Exception) {
                    println("EROARE CRITICĂ în blocul collect: ${e.message}")
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
                    // Adaugăm `key` pentru a ajuta Compose să optimizeze lista
                    items(items = movies, key = { movie -> movie.id }) { movie ->
                        MovieRow(movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                }
            }
        }
    }
}