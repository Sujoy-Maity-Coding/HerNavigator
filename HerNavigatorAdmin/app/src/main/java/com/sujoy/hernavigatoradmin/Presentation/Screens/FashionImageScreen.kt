package com.sujoy.hernavigatoradmin.Presentation.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sujoy.hernavigatoradmin.Presentation.ViewModel.ApiViewModel

@Composable
fun FashionImageScreen(viewModel: ApiViewModel) {
    val imageIds by viewModel.imageIds.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getRecentFashionImages()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(imageIds) { imageId ->
            FashionImageItem(viewModel, imageId)
        }
    }
}

@Composable
fun FashionImageItem(viewModel: ApiViewModel, imageId: Int) {
    val imageBitmaps by viewModel.imageBitmaps.observeAsState(emptyMap())

    LaunchedEffect(imageId) {
        viewModel.getFashionImage(imageId)
    }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        imageBitmaps[imageId]?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Fashion Image",
                modifier = Modifier.fillMaxSize()
            )
        } ?: Text("Loading...", fontSize = 12.sp)
    }
}