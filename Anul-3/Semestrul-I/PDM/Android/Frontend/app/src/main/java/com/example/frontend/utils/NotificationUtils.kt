package com.example.frontend.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationUtils {
    private const val CHANNEL_ID = "sync_channel_id"
    private const val CHANNEL_NAME = "Sync Notifications"

    fun createNotificationChannel(context: Context) {
        // Această funcție există doar de la API 26 (Oreo) în sus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificări pentru sincronizarea datelor"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, content: String) {
        // Verificăm permisiunea pentru Android 13+ (Tiramisu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                // Dacă nu avem permisiune, nu facem nimic (sau am putea cere permisiunea într-o activitate)
                return
            }
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Folosim un try-catch sau verificarea de permisiune de mai sus asigură că nu crăpăm
        try {
            with(NotificationManagerCompat.from(context)) {
                // Dacă IDE-ul se plânge de MissingPermission, știm că am verificat mai sus
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                ) {
                    notify(System.currentTimeMillis().toInt(), builder.build())
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}