package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        setContent {
            val navController = rememberNavController();
            //val startDestination = if (TokenManager.token != null) "list" else "login"
            NavHost(navController =  navController, startDestination = "login"){
                composable("login"){
                    LoginScreen {
                        navController.navigate("movies") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                composable("movies") {
                    MoviesScreen(
                        onMovieClick = { movieId ->
                            println("Ai dat click pe filmul cu id: $movieId")
                             navController.navigate("edit/$movieId")
                        }
                    )
                }
                composable("edit/{movieId}") { backStackEntry ->

                    val movieIdString = backStackEntry.arguments?.getString("movieId")
                    val movieId = movieIdString?.toIntOrNull()

                    if (movieId != null) {
                        MovieEditScreen(
                            movieId = movieId,
                            onUpdateSuccess = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}