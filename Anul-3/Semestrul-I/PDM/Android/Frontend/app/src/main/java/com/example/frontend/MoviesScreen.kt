package com.example.frontend

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.frontend.data.AppDatabase
import com.example.frontend.repository.MovieRepository
import com.example.frontend.service.SocketManager
import com.example.frontend.utils.NetworkUtils
import com.example.frontend.utils.ShakeDetector
import com.example.frontend.worker.SyncWorker
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(onMovieClick: (Int) -> Unit, onAddClick: () -> Unit) {
    val context = LocalContext.current


    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { MovieRepository(database.movieDao(), context) }


    val movies by repository.movies.collectAsState(initial = emptyList())


    val isOnline by NetworkUtils.observeConnectivity(context).collectAsState(initial = NetworkUtils.isInternetAvailable(context))
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        SocketManager.connect()

        repository.refreshMovies()
    }


    LaunchedEffect(Unit) {
        SocketManager.events.collect {
            println("WebSocket: Am primit notificare de modificare. Actualizez datele...")
            repository.refreshMovies()
        }
    }
    LaunchedEffect(isOnline) {
        if (isOnline) {
            println("NETUL A REVENIT! Forțăm sincronizarea datelor către server...")

            SocketManager.connect()

            repository.refreshMovies()


            val syncRequest = OneTimeWorkRequest.Builder(SyncWorker::class.java).build()
            WorkManager.getInstance(context).enqueue(syncRequest)
        }
    }
    DisposableEffect(Unit) {
        val detector = ShakeDetector(context) {
            println("Shake detectat! Facem refresh...")
            scope.launch {
                repository.refreshMovies()
                // Opțional: Vibrăm telefonul sau arătăm un mesaj
            }
        }
        detector.start()
        onDispose {
            detector.stop()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val statusText = if (isOnline) "Online" else "Offline Mode"
                    val textColor = if (isOnline) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                    Text("Lista Filme ($statusText)", color = textColor)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),

                actions = {
                    IconButton(onClick = {

                        scope.launch {

                            try { SocketManager.close()} catch (e: Exception) { e.printStackTrace() }


                            TokenManager.clear(context)

                            val intent = Intent(context, Class.forName("com.example.frontend.LoginActivity")).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)

                            (context as? Activity)?.finish()
                        }
                    }) {

                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Deconectare",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
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

            if (movies.isEmpty()) {

                Text(
                    text = "Nu există filme local. Verifică conexiunea.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items = movies, key = { movie -> movie.id }) { movie ->
                        MovieRow(movie = movie, onClick = { onMovieClick(movie.id) })
                    }
                }
            }
        }
    }
}