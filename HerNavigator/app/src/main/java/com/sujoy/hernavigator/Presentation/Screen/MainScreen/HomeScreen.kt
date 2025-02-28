package com.sujoy.hernavigator.Presentation.Screen.MainScreen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.Ads.BannerAds
import com.sujoy.hernavigator.Api.Constant
import com.sujoy.hernavigator.Api.data.Pref.UserPreferences
import com.sujoy.hernavigator.MainActivity
import com.sujoy.hernavigator.Model.HomeData
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.Presentation.FashionImage.FashionImageScreen
import com.sujoy.hernavigator.Presentation.Util.AutoImageSlider
import com.sujoy.hernavigator.Presentation.Util.DrawerContent
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.ApiViewModel
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ViewModel.NewsViewModel
import com.sujoy.hernavigator.ViewModel.SafetyViewModel
import com.sujoy.hernavigator.ui.theme.DarkRed
import com.sujoy.hernavigator.ui.theme.Pink
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    safetyViewModel: SafetyViewModel,
    context2: Context,
    imageLoader: ImageLoader,
    contextMain: MainActivity,
    fashionViewModel: ApiViewModel,
    mainViewModel: HerNavigatorViewModel,
    userPreferences: UserPreferences
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val viewModel: NewsViewModel = viewModel()
    val news by viewModel.news.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchNews("actress India", "publishedAt", Constant.newsApiKey)
    }

    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background image with gradient overlay
                Image(
                    painter = painterResource(id = R.drawable.bg),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight(0.95f)
                        .fillMaxWidth(0.86f)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFCE4EC),
                                    Color(0xFFF8BBD0)
                                )
                            ),
                            shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
                        )
                )
                DrawerContent(
                    safetyViewModel = safetyViewModel,
                    navController = navController,
                    imageList = imageList,
                    context = context,
                    modifier = Modifier
                        .fillMaxHeight(0.95f)
                        .fillMaxWidth(0.86f) // Drawer height 90% of screen
                        .padding(24.dp),
                    context2 = context2,
                    imageLoader = imageLoader,
                    userPreferences = userPreferences
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Pink,
                        titleContentColor = Color.White,
                    ),
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context2).data(data = R.drawable.menu_gif)
                                        .apply(block = {
                                            size(Size.ORIGINAL)
                                        }).build(), imageLoader = imageLoader
                                ),
                                colorFilter = ColorFilter.tint(Color.White),
                                contentDescription = "Menu",
                                modifier = Modifier.size(150.dp)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Routes.Tools)
                        }) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(context2).data(data = R.drawable.puzzle)
                                        .apply(block = {
                                            size(Size.ORIGINAL)
                                        }).build(), imageLoader = imageLoader
                                ),
                                contentDescription = "Puzzle",
                                modifier = Modifier.size(150.dp)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BannerAds(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Image(painter = painterResource(id = R.drawable.pink_bg), contentDescription = "pink", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize() // Correct approach instead of weight(1f)
                        .background(Color.Transparent)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    // AutoImageSlider as the first item
                    item {
                        AutoImageSlider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(15.dp))
                                .height(180.dp)
                        )
                    }

                    // Spacer
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    item {
                        Row(modifier=Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Our Tools", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "See All", color = DarkRed, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate(Routes.Tools) })
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        LazyRow {
                            items(imageList) {
                                Card(
                                    onClick = { navController.navigate(it.route) },
                                    modifier = Modifier.size(70.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(10.dp)
                                                .background(Pink)
                                                .align(Alignment.BottomCenter)
                                        )
                                        Image(
                                            painter = painterResource(id = it.image),
                                            contentDescription = "image",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    item {
                        Row(modifier=Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Trending Outfit", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "See All", color = DarkRed, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate(Routes.AllFashionImage) })
                        }
                    }
                    item {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(530.dp)) { // Set a finite height
                            FashionImageScreen(viewModel = fashionViewModel, navController=navController, mainViewModel=mainViewModel)
                        }
                    }
                    item {
                        Row(modifier=Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "New Actress News", color = Color.Black, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "See All", color = DarkRed, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { navController.navigate(Routes.NewsScreen) })
                        }
                    }
                    items(news.take(5)) { article ->
                        NewsItem(article, navController)
                    }
                }
                ExtendedFloatingActionButton(
                    onClick = { safetyViewModel.sendEmergencyMessageToFirstContact(context = context) },
                    icon = {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(context2).data(data = R.drawable.location_gif)
                                    .apply(block = {
                                        size(Size.ORIGINAL)
                                    }).build(), imageLoader = imageLoader
                            ),
                            "Location",
                            modifier = Modifier
                                .size(50.dp)
                                .padding(horizontal = 7.dp),
                            colorFilter = ColorFilter.tint(
                                Color.White
                            )
                        )
                    },
                    text = {
                        Text(
                            text = "Emergency\nHelp",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    containerColor = DarkRed,
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}