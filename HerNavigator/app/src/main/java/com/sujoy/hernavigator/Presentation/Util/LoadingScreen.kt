package com.sujoy.hernavigator.Presentation.Util

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.R

@Composable
fun LoadingScreen(context2: Context, imageLoader: ImageLoader) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context2).data(data = R.drawable.load_time).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = "Skin",
            modifier = Modifier.size(200.dp)
        )
    }
}