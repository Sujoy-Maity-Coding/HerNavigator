package com.sujoy.hernavigator.Presentation.Screen.Safety.Permission


import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
@Composable
fun RequestSMSPermission(onPermissionGranted: @Composable () -> Unit) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        } else {
            permissionGranted = true
        }
    }

    if (permissionGranted) {
        onPermissionGranted()
    }
}
