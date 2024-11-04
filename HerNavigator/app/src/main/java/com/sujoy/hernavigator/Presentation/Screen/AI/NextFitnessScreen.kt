package com.sujoy.hernavigator.Presentation.Screen.AI

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.Model.HomeData
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextFitnessScreen(
    modifier: Modifier = Modifier,
    viewModel: HerNavigatorViewModel,
    navController: NavHostController,
) {
    val context= LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val dataList = listOf(
        HomeData(R.drawable.excercise, "Exercise", Routes.Excercise),
        HomeData(R.drawable.diet, "Diet\nPlanner", Routes.DietPlan)
    )
    val dietPlanResult = viewModel.dietPlannerResult.collectAsState().value
    val excerciseResult=viewModel.excerciseResult.collectAsState().value
    val fitnessData = viewModel.fitnessData.observeAsState()
    val showDialog = remember { mutableStateOf(false) }
    val showDialog2= remember {
        mutableStateOf(false)
    }

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
                        text = "Fitness Tracker",
                        fontFamily = playFairDisplay,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Home)}) {
                        Icon(Icons.Default.Home, contentDescription = "home", tint = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
        ) {
            items(dataList) { item ->
                Card(
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                        .padding(top = 20.dp)
                        .height(270.dp)
                        .clickable {
                            if (item.text.contains("Diet")) {
                                // Automatically trigger diet plan generation
                                fitnessData.value?.let { data ->
                                    viewModel.generateDietPlan(
                                        height = data.height,
                                        weight = data.weight,
                                        age = data.age
                                    )
                                    Toast.makeText(context, "If you're not satisfied with this result, \nGenerate again.", Toast.LENGTH_SHORT).show()
                                }
                                showDialog.value = true // Show dialog with diet plan result
                            } else if (item.text.contains("Exercise")) {
                                // Automatically trigger diet plan generation
                                fitnessData.value?.let { data ->
                                    viewModel.generateExcercisePlan(
                                        height = data.height,
                                        weight = data.weight,
                                        age = data.age
                                    )
                                    Toast.makeText(context, "If you're not satisfied with this result, \nGenerate again.", Toast.LENGTH_SHORT).show()
                                }
                                showDialog2.value = true // Show dialog with diet plan result
                            }
                            else {
                                navController.navigate(item.route)
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Pink),
                    elevation = CardDefaults.cardElevation(10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = androidx.compose.ui.res.painterResource(id = item.image),
                            contentDescription = null
                        )
                        Text(
                            text = item.text,
                            fontFamily = playFairDisplay,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            color = Pink
                        )
                    }
                }
            }
        }

        // Display alert dialog with generated diet plan
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                confirmButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Close", color = Pink)
                    }
                },
                title = {
                    Text(
                        text = "Your Diet Plan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Pink
                    )
                },
                text = {
                    // Wrap content in a scrollable column to handle long text
                    Box(modifier = Modifier.heightIn(max = 400.dp)) { // Restrict the max height of the dialog
                        LazyColumn(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            item {
                                dietPlanResult?.let {
                                    Text(
                                        text = it.text,
                                        fontFamily = playFairDisplay,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color.White
            )
        }

        if (showDialog2.value) {
            AlertDialog(
                onDismissRequest = { showDialog2.value = false },
                confirmButton = {
                    TextButton(onClick = { showDialog2.value = false }) {
                        Text("Close", color = Pink)
                    }
                },
                title = {
                    Text(
                        text = "Your Exercise Plan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Pink
                    )
                },
                text = {
                    // Wrap content in a scrollable column to handle long text
                    Box(modifier = Modifier.heightIn(max = 400.dp)) { // Restrict the max height of the dialog
                        LazyColumn(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            item {
                                excerciseResult?.let {
                                    Text(
                                        text = it.text,
                                        fontFamily = playFairDisplay,
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color.White
            )
        }
    }
}
