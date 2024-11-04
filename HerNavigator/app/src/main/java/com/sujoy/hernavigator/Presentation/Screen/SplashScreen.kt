package com.sujoy.hernavigator.Presentation.Screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Control the visibility of the logo animation
    var startAnimation by remember { mutableStateOf(false) }

    // Trigger animation and then navigate to Home screen after 3 seconds
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000L) // 3 seconds splash screen display
        navController.navigate(Routes.Home) {
            popUpTo(Routes.SplashScreen) { inclusive = true }
        }
    }

    // Animation values: fade-in and scale-up
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    // Splash Screen Content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_splash), // Your splash logo
            contentDescription = null,
            modifier = Modifier
                .size(200.dp) // Adjust the size as per your logo
                .scale(scaleAnim.value) // Apply scale animation
                .alpha(alphaAnim.value) // Apply fade-in animation
        )
    }
}
