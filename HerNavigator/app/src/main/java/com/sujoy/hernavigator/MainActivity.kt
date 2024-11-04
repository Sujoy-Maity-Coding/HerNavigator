package com.sujoy.hernavigator

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.MobileAds
import com.sujoy.hernavigator.Presentation.NavigationGraph.App
import com.sujoy.hernavigator.ViewModel.BudgetViewModel
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ViewModel.SafetyViewModel
import com.sujoy.hernavigator.ViewModel.SelfCareViewModel
import com.sujoy.hernavigator.ViewModelFactory.BudgetViewModelFactory
import com.sujoy.hernavigator.ViewModelFactory.SafetyViewModelFactory
import com.sujoy.hernavigator.ui.theme.HerNavigatorTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var safetyViewModel: SafetyViewModel
    private lateinit var requestSmsPermissionLauncher: ActivityResultLauncher<String>
    private val budgetViewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(applicationContext)
    }

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val factory = SafetyViewModelFactory(application)
        safetyViewModel = ViewModelProvider(this, factory).get(SafetyViewModel::class.java)

        // Create notification channel for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        super.onCreate(savedInstanceState)
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }
        enableEdgeToEdge()

        val selfCareViewModel: SelfCareViewModel by viewModels()
        val viewModel: HerNavigatorViewModel by viewModels()

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        requestPermissions()

        setContent {
            HerNavigatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        safetyViewModel = safetyViewModel,
                        budgetViewModel = budgetViewModel,
                        selfCareViewModel = selfCareViewModel,
                        context = this@MainActivity
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                "self_care_channel",
                "Self-care Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for self-care reminders"
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS
        )

        // Check if exact alarms permission is needed for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                // Direct the user to App Info to enable the SCHEDULE_EXACT_ALARM permission
                openScheduleExactAlarmSettings()
            }
        }

        // Request location and SMS permissions if not already granted
        if (permissions.isNotEmpty() && !hasPermissions(*permissions.toTypedArray())) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    private fun openScheduleExactAlarmSettings() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }


    private fun hasPermissions(vararg permissions: String): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
