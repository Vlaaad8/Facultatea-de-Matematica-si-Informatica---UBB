package com.example.frontend

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.io.File


const val BASE_IMAGE_URL = "http://10.0.2.2:8081/uploads/"

@Composable
fun MovieRow(movie: Movie, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current


    val imageModel = remember(movie.imagePath) {
        val path = movie.imagePath
        when {
            path.isNullOrEmpty() -> null

            path.contains("/") -> File(path)

            else -> "$BASE_IMAGE_URL$path"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(movie.name, style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))


                    if (imageModel != null) {
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(imageModel)
                                .crossfade(true)
                                .build()
                        )


                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painter,
                                contentDescription = "Movie Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )


                            if (painter.state is AsyncImagePainter.State.Loading) {
                                CircularProgressIndicator()
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Text("Fără imagine", style = MaterialTheme.typography.bodySmall)
                    }

                    Text("Rating: ${movie.rating} ⭐")
                    Text("Data: ${movie.premierDate}")

                    Button(
                        onClick = onClick,
                        modifier = Modifier.align(Alignment.End)
                    ) { Text("Editează") }
                }
            }
        }
    }
}