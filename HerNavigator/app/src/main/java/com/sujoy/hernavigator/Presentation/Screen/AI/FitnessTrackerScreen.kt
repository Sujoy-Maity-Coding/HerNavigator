package com.sujoy.hernavigator.Presentation.Screen.AI

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.Model.FitnessData
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ui.theme.Orange
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.indieFlower
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessTrackerScreen(
    modifier: Modifier = Modifier,
    viewModel: HerNavigatorViewModel,
    navController: NavHostController
) {
    val height = remember { mutableStateOf("") }
    val weight = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
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
                        "Fitness Tracker",
                        maxLines = 1,
                        fontSize = 30.sp,
                        fontFamily = playFairDisplay,
                        fontWeight = FontWeight.Bold
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context).data(data = R.drawable.fitness_gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = "Fitness",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "\"All progress takes place outside the comfort zone.\"",
                fontSize = 20.sp,
                fontFamily = indieFlower,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = height.value, onValueChange = { height.value = it },
                label = { Text(text = "Enter your height", color = Pink) },
                placeholder = { Text(text = "e.g., 5.2 ft", color = Color.Gray) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = weight.value, onValueChange = { weight.value = it },
                label = { Text(text = "Enter Your weight", color = Pink) },
                placeholder = { Text(text = "e.g., 70 kg",color = Color.Gray) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = age.value, onValueChange = { age.value = it },
                label = { Text(text = "Enter Your age", color = Pink) },
                placeholder = { Text(text = "e.g., 25 years", color = Color.Gray) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.setFitnessData(
                        FitnessData(
                            height = height.value,
                            weight = weight.value,
                            age = age.value
                        )
                    )
                    showDialog.value = true // Show dialog first
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Text(text = "Next Page", color = Color.White, fontSize = 18.sp)
            }

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog.value = false
                            navController.navigate(Routes.NextDietScreen) // Navigate after dialog is closed
                        }) {
                            Text("Next", color = Pink)
                        }
                    },
                    title = {
                        Text(
                            text = "Remind That:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Pink
                        )
                    },
                    text = {
                        Text(
                            text = "Please note down all the plan in notebook for future.",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}
