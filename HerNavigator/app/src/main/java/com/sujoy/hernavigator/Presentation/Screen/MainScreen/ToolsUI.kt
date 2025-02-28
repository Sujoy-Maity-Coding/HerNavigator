package com.sujoy.hernavigator.Presentation.Screen.MainScreen

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.Ads.BannerAds
import com.sujoy.hernavigator.Ads.rewaredAds
import com.sujoy.hernavigator.MainActivity
import com.sujoy.hernavigator.Model.HomeData
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.Presentation.Screen.Safety.Permission.isNetworkAvailable
import com.sujoy.hernavigator.Presentation.Util.AutoImageSlider
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.SafetyViewModel
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsUI(modifier: Modifier = Modifier,
            navController: NavHostController = rememberNavController(),
            safetyViewModel: SafetyViewModel,
            context2: Context,
            imageLoader: ImageLoader,
            contextMain: MainActivity) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Search query state
    var searchQuery by remember { mutableStateOf("") }

    // Full list of items, converted to a List for easier manipulation
    val imageList = listOf(
        HomeData(R.drawable.safety, "Your Safety", Routes.YourSafety),
        HomeData(R.drawable.period, "Low Your Period Pain", Routes.PeriodPain),
        HomeData(R.drawable.prag, "Pregnancy Tracker", Routes.PregnancyTracker),
        HomeData(R.drawable.fash, "Fashion Tips", Routes.FashionTips),
        HomeData(R.drawable.skin_care, "SkinCare Routine", Routes.SkinCare),
        HomeData(R.drawable.budget, "Budget Manager", Routes.BudgetManager),
        HomeData(R.drawable.selfcare, "SelfCare Reminder", Routes.SelfCare),
        HomeData(R.drawable.haircare, "Hair Care", Routes.HairCare),
        HomeData(R.drawable.mind, "Mental Wellness", Routes.MentalWellness),
        HomeData(R.drawable.cycletracker, "Cycle Tracker", Routes.CycleTracker),
        HomeData(R.drawable.fit, "Fitness Tracker", Routes.FitnessTracker),
        HomeData(R.drawable.private_locker, "Password Locker", Routes.PrivateLocker)
    )

    // Normalize search query
    val normalizedSearchQuery = searchQuery.trim().lowercase()

    // Filtered list based on search query
    val filteredList = imageList.filter {
        it.text.lowercase().contains(normalizedSearchQuery)
    }

    // Keyboard controller to close the keyboard after search
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Pink,
                    titleContentColor = Color.White,
                ),
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { query -> searchQuery = query },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 15.sp
                        ),
                        placeholder = { Text(text = "Search Here", color = Color.White) },
                        leadingIcon = {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context2).data(data = R.drawable.search_gif).apply(block = {
                                        size(Size.ORIGINAL)
                                    }).build(), imageLoader = imageLoader
                                ),
                                colorFilter = ColorFilter.tint(Color.White),
                                contentDescription = "Search",
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(start = 10.dp)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Pink,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                            .height(50.dp)
                            .border(
                                BorderStroke(2.dp, Color.White),
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            )
        },
        bottomBar = {
            BannerAds(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Check if the filtered list is not empty
            if (filteredList.isNotEmpty()) {
                // Render the items in the filtered list
                items(filteredList.chunked(2)) { rowItems ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(0.85f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowItems.forEach { item ->
                                HomeCard(
                                    data = item,
                                    navController = navController,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(bottom = 16.dp)
                                        .height(170.dp),
                                    contextMain = contextMain,
                                    context = context2
                                )
                            }
                            // Add Spacer if there's only one item in the row
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            } else {
                // Show "No Results Found" message when no items match the search query
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No Results Found",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeCard(
    data: HomeData,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contextMain: MainActivity,
    context: Context
) {
    Card(
        modifier = modifier
            .clickable {
                if (data.text.contains("SkinCare")) {
                    if (isNetworkAvailable(context)) {
                        navController.navigate(Routes.loadingScreen) // Show loading screen
                        rewaredAds(
                            activity = contextMain,
                            navController = navController,
                            onAdLoaded = {
                                navController.popBackStack() // Remove the loading screen from the stack
                                navController.navigate(Routes.SkinCare)
                            },
                            onAdFailed = {
                                navController.popBackStack() // Remove the loading screen from the stack
                                navController.navigate(Routes.SkinCare)
                            }
                        )
                    } else {
                        navController.navigate(Routes.noNetwork)
                    }
                } else {
                    navController.navigate(data.route)
                }},
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, Pink),
        shape = RoundedCornerShape(30.dp),
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
                painter = painterResource(id = data.image),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = data.text,
                fontFamily = playFairDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Pink,
                maxLines = 2, // Ensure text stays within two lines
                overflow = TextOverflow.Ellipsis, // Truncate the overflowed text
                textAlign = TextAlign.Center, // Center the text inside the card
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}