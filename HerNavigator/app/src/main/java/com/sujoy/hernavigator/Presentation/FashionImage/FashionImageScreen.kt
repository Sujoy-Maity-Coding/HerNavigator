package com.sujoy.hernavigator.Presentation.FashionImage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.ViewModel.ApiViewModel
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel

@Composable
fun FashionImageScreen(
    viewModel: ApiViewModel,
    navController: NavHostController,
    mainViewModel: HerNavigatorViewModel
) {
    val imageIds by viewModel.imageIds.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.getRecentFashionImages()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .background(Color.Transparent)
    ) {
        items(imageIds) { imageId ->
            FashionImageItem(viewModel, imageId, navController, mainViewModel)
        }
    }
}