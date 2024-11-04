package com.sujoy.hernavigator.Presentation.Screen.AI

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.R

@Composable
fun NoNetworkScreen(modifier: Modifier = Modifier, context2: Context, imageLoader: ImageLoader, onRetry: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context2).data(data = R.drawable.offline_gif).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = "offline",
            modifier = Modifier.size(250.dp)
        )
        Text(text = "No Internet Access", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
        Text(text = "Check Your Internet Connection", fontSize = 15.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context2).data(data = R.drawable.retry_gif).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = "Retry",
            modifier = Modifier.size(50.dp).clickable { onRetry() }
        )
        Text(text = "Retry Again 🔼", fontSize = 15.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.SansSerif)
    }
}