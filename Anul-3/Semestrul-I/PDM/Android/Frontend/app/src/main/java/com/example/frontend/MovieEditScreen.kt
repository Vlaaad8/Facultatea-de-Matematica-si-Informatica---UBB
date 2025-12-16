package com.example.frontend

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.frontend.data.AppDatabase
import com.example.frontend.repository.MovieRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieEditScreen(movieId: Int, onUpdateSuccess: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Inițializăm Repository-ul
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { MovieRepository(database.movieDao(), context) }

    // State-uri pentru formular
    var name by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var movieObj by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // Încărcăm datele din BAZA DE DATE LOCALĂ (Room)
    // Asta permite deschiderea ecranului și editarea chiar și fără internet!
    LaunchedEffect(movieId) {
        val entity = database.movieDao().getMovieById(movieId)
        if (entity != null) {
            // Convertim din Entity (DB) în Movie (Model)
            movieObj = entity.toMovie()

            // Populăm câmpurile formularului
            name = movieObj!!.name
            rating = movieObj!!.rating.toString()
            isRunning = movieObj!!.running == 1
            selectedDate = movieObj!!.premierDate
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Editează Filmul", style = MaterialTheme.typography.headlineMedium)

            if (isLoading) {
                Spacer(modifier = Modifier.height(32.dp))
                CircularProgressIndicator()
            } else if (movieObj != null) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nume Film") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text("Rating") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dateFormatter.format(selectedDate),
                    onValueChange = { },
                    label = { Text("Data Premierei") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDatePicker = true
                                    }
                                }
                            }
                        },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Rulează în cinema?", modifier = Modifier.weight(1f))
                    Switch(checked = isRunning, onCheckedChange = { isRunning = it })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            // Creăm obiectul actualizat
                            val updatedMovie = movieObj!!.copy(
                                name = name,
                                rating = rating.toDoubleOrNull() ?: 0.0,
                                running = if (isRunning) 1 else 0,
                                premierDate = selectedDate
                            )

                            // Apelăm repository-ul pentru salvare (Sync sau Offline fallback)
                            repository.editMovie(movieId, updatedMovie)

                            // Ne întoarcem la listă
                            onUpdateSuccess()
                        }
                    }
                ) {
                    Text("Salvează Modificările")
                }
            } else {
                // Caz rar: Filmul nu a fost găsit în DB local
                Text("Eroare: Filmul nu a fost găsit local.")
            }
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate.time
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (datePickerState.selectedDateMillis != null) {
                                selectedDate = Date(datePickerState.selectedDateMillis!!)
                            }
                            showDatePicker = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}