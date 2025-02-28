package com.sujoy.hernavigator.Presentation.NavigationGraph

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.sujoy.hernavigator.Ads.rewaredAds
import com.sujoy.hernavigator.Api.data.Pref.UserPreferences
import com.sujoy.hernavigator.MainActivity
import com.sujoy.hernavigator.Presentation.FashionImage.AllFashionImageScreen
import com.sujoy.hernavigator.Presentation.FashionImage.FashionDetailsScreen
import com.sujoy.hernavigator.Presentation.Screen.BudgetPlanner.BudgetApp
import com.sujoy.hernavigator.Presentation.Screen.AI.ColourMatchScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.CycleTrackerScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.FashionTipsScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.FitnessTrackerScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.HairCareScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.MentalWellnessScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.NextFitnessScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.NoNetworkScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.OccasionScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.PeriodPainScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.PimpleReduction
import com.sujoy.hernavigator.Presentation.Screen.AI.PregnencyTrackerScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.SkinCare
import com.sujoy.hernavigator.Presentation.Screen.AI.SkinToneScreen
import com.sujoy.hernavigator.Presentation.Screen.AI.WeatherWiseSkin
import com.sujoy.hernavigator.Presentation.Screen.PasswordStorage.PasswordApp
import com.sujoy.hernavigator.Presentation.Screen.SelfCare.TaskSelectionScreen
import com.sujoy.hernavigator.Presentation.Screen.Safety.WomanSafetyApp
import com.sujoy.hernavigator.Presentation.Screen.AI.SkinGlow
import com.sujoy.hernavigator.Presentation.Screen.MainScreen.HomeScreen
import com.sujoy.hernavigator.Presentation.Screen.MainScreen.NewsApp
import com.sujoy.hernavigator.Presentation.Screen.MainScreen.ProfileScreen
import com.sujoy.hernavigator.Presentation.Screen.MainScreen.ToolsUI
import com.sujoy.hernavigator.Presentation.Screen.Safety.Permission.isNetworkAvailable
import com.sujoy.hernavigator.Presentation.Screen.SignUpIn.SignInUi
import com.sujoy.hernavigator.Presentation.Screen.SignUpIn.SignUpUi
import com.sujoy.hernavigator.Presentation.Util.LoadingScreen
import com.sujoy.hernavigator.ViewModel.ApiViewModel
import com.sujoy.hernavigator.ViewModel.BudgetViewModel
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ViewModel.SafetyViewModel
import com.sujoy.hernavigator.ViewModel.SelfCareViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App(
    modifier: Modifier = Modifier,
    viewModel: HerNavigatorViewModel,
    safetyViewModel: SafetyViewModel,
    budgetViewModel: BudgetViewModel,
    selfCareViewModel: SelfCareViewModel,
    context: MainActivity,
    fashionViewModel: ApiViewModel,
    navController: NavHostController,
    userPreferences: UserPreferences
) {
    val context2 = LocalContext.current
    val imageLoader = ImageLoader.Builder(context2)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    NavHost(navController = navController, startDestination = Routes.Login) {
        composable<Routes.SignUp> {
            SignUpUi(navController=navController, userPreferences = userPreferences)
        }
        composable<Routes.Login> {
            SignInUi(navController=navController, userPreferences = userPreferences)
        }
        composable<Routes.NewsScreen> {
            NewsApp(navController=navController)
        }
        composable<Routes.Tools> {
            ToolsUI(navController = navController,
                contextMain = context,
                modifier = Modifier.background(Color.White),
                safetyViewModel = safetyViewModel,
                context2 = context2,
                imageLoader = imageLoader)
        }
        composable<Routes.Profile>{ ProfileScreen()}
        composable<Routes.Home> {
            HomeScreen(
                navController = navController,
                contextMain = context,
                modifier = Modifier.background(Color.White),
                safetyViewModel = safetyViewModel,
                context2 = context2,
                imageLoader = imageLoader,
                fashionViewModel=fashionViewModel,
                mainViewModel = viewModel,
                userPreferences = userPreferences
            )
        }
        composable<Routes.MentalWellness> {
            MentalWellnessScreen(
                viewModel = viewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.PregnancyTracker> {
            PregnencyTrackerScreen(
                viewModel = viewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.YourSafety> {
            WomanSafetyApp(safetyViewModel = safetyViewModel,navController = navController)
        }
        composable<Routes.PeriodPain> {
            PeriodPainScreen(
                viewModel = viewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.SkinCare> {
            SkinCare(navController = navController,)
        }
        composable<Routes.BudgetManager> {
            BudgetApp(
                viewModel = budgetViewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.FashionTips> {
            FashionTipsScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.background(Color.White)
            )
        }
        composable<Routes.ColourMatch> {
            ColourMatchScreen(viewModel = viewModel, modifier = Modifier.background(Color.White),navController = navController)
        }
        composable<Routes.SkinToneDress> {
            SkinToneScreen(viewModel = viewModel, modifier = Modifier.background(Color.White),navController = navController)
        }
        composable<Routes.OccasionDress> {
            OccasionScreen(
                viewModel = viewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.SelfCare> {
            TaskSelectionScreen(viewModel = selfCareViewModel, context = context,navController = navController)
        }
        composable<Routes.HairCare> {
            HairCareScreen(
                viewModel = viewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.CycleTracker> {
            CycleTrackerScreen(
                viewModel = viewModel,
                modifier = Modifier.background(Color.White),
                context2 = context2,
                imageLoader = imageLoader,
                navController = navController
            )
        }
        composable<Routes.FitnessTracker> {
            FitnessTrackerScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier.background(Color.White)
            )
        }
        composable<Routes.PrivateLocker> {
            PasswordApp(navController = navController)
        }
        composable<Routes.NextDietScreen> {
            NextFitnessScreen(viewModel = viewModel, navController = navController)
        }
        composable<Routes.weatherSkin> {
            WeatherWiseSkin(viewModel = viewModel, context2 = context2, imageLoader = imageLoader,navController = navController)
        }
        composable<Routes.pimpleReduction> {
            PimpleReduction(viewModel = viewModel,navController = navController)
        }
        composable<Routes.skinGlow> {
            SkinGlow(viewModel = viewModel,navController = navController)
        }
        composable<Routes.noNetwork> {
            NoNetworkScreen(
                context2 = context2,
                imageLoader = imageLoader,
                onRetry = {
                    // Retry logic when network becomes available
                    if (isNetworkAvailable(context2)) {
                        navController.navigateUp() // Close NoNetworkScreen
                        navController.navigate(Routes.loadingScreen) // Navigate to loading screen and retry
                        rewaredAds(
                            activity = context,
                            navController = navController,
                            onAdLoaded = {
                                navController.popBackStack()
                                navController.navigate(Routes.SkinCare)
                            },
                            onAdFailed = {
                                navController.popBackStack()
                                Toast.makeText(context2, "Ad failed to load.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    } else {
                        Toast.makeText(context2, "Network still unavailable", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
        composable<Routes.loadingScreen> {
            LoadingScreen(context2 = context2, imageLoader = imageLoader)
        }
        composable<Routes.AllFashionImage>{ AllFashionImageScreen(viewModel = fashionViewModel, navController=navController, mainViewModel=viewModel) }
        composable<Routes.FashionDetails>{ FashionDetailsScreen(viewModel = viewModel, navController = navController) }
    }
}

// Extension function to serialize/deserialize routes
//inline fun <reified T : Routes> NavHostController.navigate(route: T) {
//    val routeJson = Json.encodeToString(route)
//    this.navigate(routeJson)
//}
//
//inline fun <reified T : Routes> NavBackStackEntry.toRoute(): T {
//    val routeJson = arguments?.getString("route")
//    return Json.decodeFromString(routeJson ?: "")
//}