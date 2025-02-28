package com.sujoy.hernavigator.Presentation.Screen.AI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sujoy.hernavigator.Model.HomeData
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinCare(modifier: Modifier = Modifier,
             navController: NavHostController = rememberNavController()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val dataLsit = listOf(
        HomeData(R.drawable.pimple, "Pimple Reduction", Routes.pimpleReduction),
        HomeData(R.drawable.skin, "Weather Wise Routine", Routes.weatherSkin),
        HomeData(R.drawable.skin_glow, "Glow Your Skin", Routes.skinGlow)
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Pink,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        text = "SkinCare Routine",
                        fontFamily = playFairDisplay,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Home){
                        popUpTo(navController.graph.startDestinationId) { inclusive = true } // Clears back stack up to start destination
                        launchSingleTop = true  // Prevents duplicate instances
                    }}) {
                        Icon(Icons.Default.Home, contentDescription = "home", tint = Color.White)
                    }
                }
            )
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
        ) {
            items(dataLsit) {
                Card(
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                        .padding(top = 20.dp)
                        .height(270.dp)
                        .clickable {
                            navController.navigate(it.route)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Pink),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = it.image),
                            contentDescription = null
                        )
                        Text(
                            text = it.text,
                            fontFamily = playFairDisplay,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = Pink,
                            maxLines = 2, // Ensure text stays within two lines
                            overflow = TextOverflow.Ellipsis, // Truncate the overflowed text
                            textAlign = TextAlign.Center, // Center the text inside the card
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}