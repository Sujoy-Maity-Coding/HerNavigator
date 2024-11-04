package com.sujoy.hernavigator.Presentation.Screen.BudgetPlanner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sujoy.hernavigator.MainActivity
import com.sujoy.hernavigator.R

fun sendBudgetExceededNotification(context: Context) {
    val channelId = "budget_exceeded_channel"
    val notificationId = 1

    // Use the default notification sound
    val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    // Create a notification channel for Android 8.0+ with default sound
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Budget Exceeded"
        val descriptionText = "Notifications for when the budget is exceeded"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
            setSound(defaultSoundUri, null) // Use the default sound
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Create an explicit intent for an Activity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    // Build the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.lg) // Your app's notification icon
        .setContentTitle("Budget Exceeded")
        .setContentText("You have exceeded your weekly budget.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setSound(defaultSoundUri) // Set default sound for Android < 8.0
        .setContentIntent(pendingIntent)

    // Show the notification
    with(NotificationManagerCompat.from(context)) {
        try {
            notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // Handle the case when the permission is not granted
            e.printStackTrace()
        }
    }
}