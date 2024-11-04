package com.sujoy.hernavigator.Presentation.Screen.BudgetPlanner

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.BudgetViewModel
import com.sujoy.hernavigator.ui.theme.Orange
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.indieFlower
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetApp(
    viewModel: BudgetViewModel,
    modifier: Modifier,
    context2: Context,
    imageLoader: ImageLoader,
    navController: NavHostController
) {
    val context = LocalContext.current
    var dailyBudgetInput by remember { mutableStateOf("") } // This holds the temporary weekly budget input
    val dailyBudget by viewModel.dailyBudget.collectAsState() // Weekly budget from ViewModel
    val totalSpending by viewModel.totalSpending.collectAsState() // Total spending from ViewModel

    var moneySpending by remember { mutableStateOf("") } // Daily spending input
    val remainingBudget =
        (dailyBudget.toDoubleOrNull() ?: 0.0) - totalSpending // Remaining budget calculation

    var showDailyBudgetToast by remember { mutableStateOf(false) }
    var showMoneySpendingToast by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Show toast for weekly budget saved
    LaunchedEffect(showDailyBudgetToast) {
        if (showDailyBudgetToast) {
            Toast.makeText(context, "Daily budget saved!", Toast.LENGTH_SHORT).show()
            showDailyBudgetToast = false // Reset after showing toast
        }
    }

    // Show toast for daily spending added
    LaunchedEffect(showMoneySpendingToast) {
        if (showMoneySpendingToast) {
            Toast.makeText(context, "Spending money added!", Toast.LENGTH_SHORT).show()
            showMoneySpendingToast = false // Reset after showing toast
        }
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Pink,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        "Budget Manager",
                        maxLines = 1,
                        fontSize = 30.sp,
                        fontFamily = playFairDisplay,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Home)}) {
                        Icon(Icons.Default.Home, contentDescription = "home", tint = Color.White)
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            // Weekly budget input or display
            if (dailyBudget.isEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context2).data(data = R.drawable.budget_gif).apply(block = {
                            size(Size.ORIGINAL)
                        }).build(), imageLoader = imageLoader
                    ),
                    contentDescription = "Budget",
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = "\"When you set a budget, you are taking control of your future.\"",
                    fontSize = 20.sp,
                    fontFamily = indieFlower,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = dailyBudgetInput, onValueChange = { dailyBudgetInput = it },
                    label = { Text(text = "Enter Amount", color = Pink) },
                    placeholder = { Text(text = "How many amount you want to spend per day?") },
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Pink,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true, // Ensures input stays on a single line
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (dailyBudgetInput.isNotEmpty()) {
                            viewModel.setDailyBudget(dailyBudgetInput) // Save the weekly budget in ViewModel only when user presses the button
                            showDailyBudgetToast = true
                            dailyBudgetInput = "" // Clear the input after saving
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Pink),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Daily Budget", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                        .border(2.dp, Pink, shape = RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Spending Budget : ",
                                color = Orange,
                                fontFamily = playFairDisplay,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dailyBudget,
                                color = Orange,
                                fontFamily = indieFlower,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Total Budget : ",
                                color = Color.Green,
                                fontFamily = playFairDisplay,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$totalSpending",
                                color = Color.Green,
                                fontFamily = indieFlower,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Remaining Budget : ",
                                color = Color.Red,
                                fontFamily = playFairDisplay,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$remainingBudget",
                                color = Color.Red,
                                fontFamily = indieFlower,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = moneySpending, onValueChange = { moneySpending = it },
                    label = { Text(text = "Enter Amount", color = Pink) },
                    placeholder = { Text(text = "How many amount you spend?") },
                    colors = TextFieldDefaults.colors(
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Pink,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    singleLine = true, // Ensures input stays on a single line
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val dailyAmount = moneySpending.toDoubleOrNull() ?: 0.0
                        viewModel.addMoneySpending(dailyAmount)

                        // Show daily spending toast
                        showMoneySpendingToast = true
                        moneySpending = "" // Clear the input after adding

                        // Check if total spending exceeds weekly budget and notify
                        if (viewModel.totalSpending.value > dailyBudget.toDoubleOrNull() ?: 0.0) {
                            sendBudgetExceededNotification(context)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Pink),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Spending", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}