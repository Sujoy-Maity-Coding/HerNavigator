package com.sujoy.hernavigator.Presentation.Screen.SelfCare

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

@SuppressLint("ScheduleExactAlarm")
fun scheduleReminder(context: Context, task: String, time: String, duration: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val (hour, minute, isPm) = parseTimeAmPm(time)

    // Intent for AlarmReceiver - Start Notification
    val startIntent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("TASK_NAME", task)
        putExtra("NOTIFICATION_TYPE", "START")
    }
    val startPendingIntent = PendingIntent.getBroadcast(
        context,
        task.hashCode(),
        startIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Intent for AlarmReceiver - End Notification
    val endIntent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("TASK_NAME", task)
        putExtra("NOTIFICATION_TYPE", "END")
    }
    val endPendingIntent = PendingIntent.getBroadcast(
        context,
        task.hashCode() + 1,
        endIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, if (isPm && hour < 12) hour + 12 else hour)
        set(Calendar.MINUTE, minute)
    }

    // Set the alarm for the start of the task
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        startPendingIntent
    )

    // Set the alarm for when the task duration ends
    val endTime = calendar.timeInMillis + duration * 60 * 1000L
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        endTime,
        endPendingIntent
    )
}


// Helper function to parse AM/PM time
fun parseTimeAmPm(time: String): Triple<Int, Int, Boolean> {
    val isPm = time.endsWith("PM")
    val timeParts = time.replace("AM", "").replace("PM", "").trim().split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1].toInt()
    return Triple(hour, minute, isPm)
}
