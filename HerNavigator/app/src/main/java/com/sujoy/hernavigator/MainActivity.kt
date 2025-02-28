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
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.sujoy.hernavigator.Api.data.Pref.UserPreferences
import com.sujoy.hernavigator.Presentation.NavigationGraph.App
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.Presentation.Screen.OnBoardinScreen.OnboardingPreferences
import com.sujoy.hernavigator.Presentation.Screen.OnBoardinScreen.OnboardingScreen
import com.sujoy.hernavigator.Presentation.Util.NotificationWorker
import com.sujoy.hernavigator.ViewModel.ApiViewModel
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
    private lateinit var onboardingPreferences: OnboardingPreferences
    private lateinit var safetyViewModel: SafetyViewModel
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        onboardingPreferences = OnboardingPreferences(this)
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
        NotificationWorker.scheduleNotificationWorker(this)
        testNotificationWorker()

        val userPreferences = UserPreferences(this)

        setContent {
            var showOnboarding by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            // Observe onboarding state
            LaunchedEffect(Unit) {
                onboardingPreferences.isOnboardingComplete.collect { isComplete ->
                    showOnboarding = !isComplete
                }
            }
            HerNavigatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context= LocalContext.current
                    val viewModelFashion = remember { ApiViewModel() }
                    if (showOnboarding) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                            contentAlignment = Alignment.Center) {
                            OnboardingScreen(onFinishOnboarding = {
                                scope.launch {
                                    onboardingPreferences.setOnboardingComplete()
                                }
                                showOnboarding = false
                            })
                        }
                    } else {
                        val navController = rememberNavController()

                        LaunchedEffect(Unit) {
                            val userId = userPreferences.getUserId()
                            if (userId != null) {
                                if (userId.isNotEmpty()) {
                                    navController.navigate(Routes.Home) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
                        }
                        App(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel,
                            safetyViewModel = safetyViewModel,
                            budgetViewModel = budgetViewModel,
                            selfCareViewModel = selfCareViewModel,
                            fashionViewModel= viewModelFashion,
                            context = this@MainActivity,
                            navController = navController,
                            userPreferences = userPreferences
                        )
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val selfCareChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        val appChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                "notification_channel",
                "App Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for app notifications"
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(appChannel)
        notificationManager.createNotificationChannel(selfCareChannel)
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
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
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

    private fun testNotificationWorker() {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
        Log.d("MainActivity", "Manual Notification Worker Triggered")
    }


    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
