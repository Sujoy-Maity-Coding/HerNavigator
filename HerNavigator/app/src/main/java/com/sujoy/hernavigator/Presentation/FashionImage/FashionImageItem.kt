package com.sujoy.hernavigator.Presentation.FashionImage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.ViewModel.ApiViewModel
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel

@Composable
fun FashionImageItem(
    viewModel: ApiViewModel,
    imageId: Int,
    navController: NavHostController,
    mainViewModel: HerNavigatorViewModel
) {
    val imageBitmaps by viewModel.imageBitmaps.observeAsState(emptyMap())

    LaunchedEffect(imageId) {
        viewModel.getFashionImage(imageId)
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
            .clickable {
                imageBitmaps[imageId]?.let { bitmap ->
                    mainViewModel.setSelectedImage(bitmap) // Save selected image
                    mainViewModel.fashionGenerator(bitmap) // Call AI analysis
                    navController.navigate(Routes.FashionDetails) // Navigate to result screen
                }
            },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            imageBitmaps[imageId]?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Fashion Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Text(
                "Loading...", color = Color.Gray, fontSize = 12.sp
            )
        }
    }
}