package com.example.frontend

import androidx.compose.foundation.clickable
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieEditScreen(movieId: Int, onUpdateSuccess: () -> Unit) {
    val scope = rememberCoroutineScope()


    var name by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }


    var selectedDate by remember { mutableStateOf(Date()) }
    var showDatePicker by remember { mutableStateOf(false) }

    var movieObj by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("") }


    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    LaunchedEffect(movieId) {
        try {
            val response = RetrofitClient.movieService.getMovieDetail(movieId)
            if (response.isSuccessful && response.body() != null) {
                movieObj = response.body()
                name = movieObj!!.name
                rating = movieObj!!.rating.toString()
                isRunning = movieObj!!.running == 1
                selectedDate = movieObj!!.premierDate
            } else {
                message = "Eroare încărcare: ${response.code()}"
            }
        } catch (e: Exception) {
            message = "Eroare rețea: ${e.message}"
        } finally {
            isLoading = false
        }
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
                    Text("Running?", modifier = Modifier.weight(1f))
                    Switch(checked = isRunning, onCheckedChange = { isRunning = it })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            try {
                                val updatedMovie = movieObj!!.copy(
                                    name = name,
                                    rating = rating.toDoubleOrNull() ?: 0.0,
                                    running = if (isRunning) 1 else 0,
                                    premierDate = selectedDate
                                )

                                val response = RetrofitClient.movieService.editMovie(movieId, updatedMovie)
                                if (response.isSuccessful) {
                                    onUpdateSuccess()
                                } else {
                                    message = "Eroare server: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                message = "Eroare: ${e.message}"
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            }

            if (message.isNotEmpty()) {
                Text(message, color = MaterialTheme.colorScheme.error)
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
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}