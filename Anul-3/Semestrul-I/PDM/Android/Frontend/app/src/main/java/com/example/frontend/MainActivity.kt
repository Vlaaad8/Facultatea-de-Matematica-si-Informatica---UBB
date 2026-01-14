package com.example.frontend

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.frontend.utils.NotificationUtils
import com.example.frontend.utils.ShakeDetector
import com.example.frontend.worker.SyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TokenManager.loadToken(applicationContext)

        NotificationUtils.createNotificationChannel(applicationContext)

        setupPeriodicSync()
        shakeDetector = ShakeDetector(this) {

            showShakeMessage()
        }
        setContent {
            val navController = rememberNavController()


            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    println("Permisiune notificări acordată: $isGranted")
                }
            )

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            val startDestination = if (TokenManager.token != null) "movies" else "login"

            NavHost(navController = navController, startDestination = startDestination) {
                composable("login") {
                    LoginScreen {
                        navController.navigate("movies") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                composable("movies") {
                    MoviesScreen(
                        onMovieClick = { movieId ->
                            navController.navigate("edit/$movieId")
                        },
                        onAddClick = {
                            navController.navigate("add")
                        }
                    )
                }
                composable("add") {
                    MovieAddScreen(
                        onAddSuccess = { navController.popBackStack() },
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable("edit/{movieId}") { backStackEntry ->
                    val movieIdString = backStackEntry.arguments?.getString("movieId")
                    val movieId = movieIdString?.toIntOrNull()
                    if (movieId != null) {
                        MovieEditScreen(
                            movieId = movieId,
                            onUpdateSuccess = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
    private fun showShakeMessage() {
        Toast.makeText(this, "Shake detectat!", Toast.LENGTH_SHORT).show()

    }


    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }


    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

    private fun setupPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MovieSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}