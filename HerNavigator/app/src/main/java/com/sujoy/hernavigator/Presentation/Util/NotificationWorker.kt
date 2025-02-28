package com.sujoy.hernavigator.Presentation.Util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sujoy.hernavigator.Api.api.api_image_noti.RetrofitClient
import com.sujoy.hernavigator.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val client = OkHttpClient()

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo()) // ✅ Fixed issue

        return try {
            fetchLatestNotification()
            Result.success()
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error fetching notifications: ${e.message}")
            Result.retry()
        }
    }

    private suspend fun fetchLatestNotification() {
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://sujoydev.pythonanywhere.com/notifications")
                .build()

            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("NotificationWorker", "API Request Failed: ${response.code}")
                        return@use
                    }

                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val notifications = JSONArray(it)
                        if (notifications.length() > 0) {
                            val latestNotification = notifications.getJSONObject(notifications.length() - 1)
                            val latestMessage = latestNotification.getString("notification")

                            val sharedPrefs = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                            val lastNotification = sharedPrefs.getString("last_notification", "")

                            if (latestMessage != lastNotification) {
                                showNotification(latestMessage)
                                sharedPrefs.edit().putString("last_notification", latestMessage).apply()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                Log.e("NotificationWorker", "Network error: ${e.message}")
            }
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notification_channel",
                "App Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for app notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, "notification_channel")
            .setSmallIcon(R.drawable.lg)
            .setContentTitle("New Notification")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)

        notificationManager.notify(Random.nextInt(), builder.build())
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, "notification_channel")
            .setSmallIcon(R.drawable.lg)
            .setContentTitle("Checking for Notifications")
            .setContentText("Fetching latest notifications in the background...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return ForegroundInfo(
            Random.nextInt(),
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING // ✅ Fixed foreground service type
        )
    }

    companion object {
        fun scheduleNotificationWorker(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "notification_worker",
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
            Log.d("NotificationWorker", "Notification Worker Scheduled")
        }
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device reboot detected! Restarting WorkManager...")
            NotificationWorker.scheduleNotificationWorker(context)
        }
    }
}

