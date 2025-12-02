package com.example.frontend

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieAddScreen(
    onAddSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val dateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adaugă Film Nou") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Înapoi")
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


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
                            Icon(Icons.Default.DateRange, contentDescription = "Dată")
                        }
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) showDatePicker = true
                                }
                            }
                        },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Rulează în cinema?", modifier = Modifier.weight(1f))
                    Switch(checked = isRunning, onCheckedChange = { isRunning = it })
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            try {
                                val newMovie = Movie(
                                    id = 0,
                                    owner_id = 0,
                                    name = name,
                                    rating = rating.toDoubleOrNull() ?: 0.0,
                                    running = if (isRunning) 1 else 0,
                                    premierDate = selectedDate
                                )

                                val response = RetrofitClient.movieService.addMovie(newMovie)

                                if (response.isSuccessful) {
                                    onAddSuccess()
                                } else {
                                    message = "Eroare: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                message = "Eroare: ${e.message}"
                            }
                        }
                    }
                ) {
                    Text("Adaugă Filmul")
                }

                if (message.isNotEmpty()) {
                    Text(message, color = MaterialTheme.colorScheme.error)
                }
            }

            // Dialogul Calendarului
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (datePickerState.selectedDateMillis != null) {
                                selectedDate = Date(datePickerState.selectedDateMillis!!)
                            }
                            showDatePicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Anulează") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}