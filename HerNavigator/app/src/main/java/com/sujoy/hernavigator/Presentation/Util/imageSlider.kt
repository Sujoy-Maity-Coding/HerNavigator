package com.sujoy.hernavigator.Presentation.Util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ui.theme.Pink
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoImageSlider(modifier: Modifier) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-slide logic
    LaunchedEffect(pagerState.currentPage) {
        coroutineScope.launch {
            delay(3000) // 3 seconds delay between slides
            val nextPage = (pagerState.currentPage + 1) % imageList.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            count = imageList.size,
            state = pagerState,
            modifier = modifier
        ) { page ->
            Image(
                painter = painterResource(id = imageList[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dot Indicator
        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Pink,
            inactiveColor = Color.Gray,
            indicatorWidth = 10.dp,
            indicatorHeight = 10.dp,
            spacing = 8.dp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp)
        )
    }
}

// Sample drawable image list (replace with your own drawable resources)
val imageList = listOf(
    R.drawable.img1, // Replace with your drawable resources
    R.drawable.img2,
    R.drawable.img3,
    R.drawable.img4,
)

@Preview(showBackground = true)
@Composable
fun PreviewImageSlider() {
    AutoImageSlider(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
