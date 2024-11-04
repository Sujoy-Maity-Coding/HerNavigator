package com.sujoy.hernavigator.Presentation.Screen.SelfCare

import android.annotation.SuppressLint

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sujoy.hernavigator.R

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val taskName = intent.getStringExtra("TASK_NAME") ?: "Self-care task"
        val notificationType = intent.getStringExtra("NOTIFICATION_TYPE") ?: "START"
        val notificationMessage = when (notificationType) {
            "START" -> "It's time for your $taskName!"
            "END" -> "You've completed your $taskName session!"
            else -> "Reminder"
        }

        val notification = NotificationCompat.Builder(context, "self_care_channel")
            .setSmallIcon(R.drawable.lg)
            .setContentTitle("Self-care Reminder")
            .setContentText(notificationMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(taskName.hashCode(), notification)
    }
}
