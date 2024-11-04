package com.sujoy.hernavigator.Presentation.Util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ui.theme.FadeBlue
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@Preview(showSystemUi = true)
@Composable
fun IntroductoryOverlay(onSkip: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 60.dp)
            .background(Color(0x80000000)), // semi-transparent background
        contentAlignment = Alignment.TopEnd
    ) {
        // Small box near "location" button with description
        Box(modifier = Modifier.fillMaxWidth().padding(end = 10.dp)) {
            Image(
                painter = painterResource(id = R.drawable.msg),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .padding(top = 50.dp, end = 25.dp, start = 50.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tap for instant help—\nthis button alerts your trusted contact via 'Your Safety' tools.",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = playFairDisplay,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onSkip,
                    colors = ButtonDefaults.buttonColors(containerColor = FadeBlue)
                ) {
                    Text(
                        "Skip",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}