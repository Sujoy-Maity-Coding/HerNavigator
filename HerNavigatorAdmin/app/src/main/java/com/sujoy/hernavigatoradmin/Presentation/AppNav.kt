package com.sujoy.hernavigatoradmin.Presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sujoy.hernavigatoradmin.Presentation.Screens.NotificationScreen
import com.sujoy.hernavigatoradmin.Presentation.Screens.UploadImageScreen
import com.sujoy.hernavigatoradmin.Presentation.ViewModel.ApiViewModel
import com.sujoy.hernavigatoradmin.ui.theme.Pink

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNav(viewModel: ApiViewModel, innerPadding: PaddingValues) {
    var selectedIndex by remember { mutableStateOf(0) }
    val bottomBarItems = listOf(
        BottomBarItem("Notification", Icons.Default.Notifications),
        BottomBarItem("Add Product", Icons.Default.Add)
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar( modifier = Modifier.height(100.dp),
                    containerColor = Pink) {
                    bottomBarItems.forEachIndexed { index, bottomBarItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = { Image(imageVector = bottomBarItem.icon, contentDescription = null, colorFilter = ColorFilter.tint(Color.Black)) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.White
                            ))
                    }
                }
            }
        ) {
            when(selectedIndex){
                0 -> NotificationScreen(viewModel = viewModel)
                1 -> UploadImageScreen(viewModel = viewModel)
            }
        }
    }
}

data class BottomBarItem(val name: String, val icon: ImageVector)