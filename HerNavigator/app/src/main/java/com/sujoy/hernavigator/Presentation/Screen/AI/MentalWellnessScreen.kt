package com.sujoy.hernavigator.Presentation.Screen.AI

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
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
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ui.theme.Blue
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.indieFlower
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentalWellnessScreen(
    modifier: Modifier = Modifier,
    viewModel: HerNavigatorViewModel,
    context2: Context,
    imageLoader: ImageLoader,
    navController: NavHostController
) {
    val mood = remember { mutableStateOf("") }
    val textResult = viewModel.mentalWellnessResult.collectAsState().value

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
                        "Mental Wellness",
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
                    ImageRequest.Builder(context2).data(data = R.drawable.mental_gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                contentDescription = "Mental",
                modifier = Modifier.padding(top = 15.dp)
            )
            Text(
                text = "\"There is hope, even when your brain tells you there isn’t.\"",
                fontSize = 20.sp,
                fontFamily = indieFlower,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Blue
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = mood.value, onValueChange = { mood.value = it },
                label = { Text(text = "Enter Your Mood", color = Pink) },
                placeholder = { Text(text = "How are you feeling today?", color = Color.Gray) },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(painter = painterResource(id = R.drawable.sent), contentDescription = null,
                modifier = Modifier.size(60.dp).clickable {
                    viewModel.mentalWellnessGenerator(mood.value)
                    Toast.makeText(context2, "If you're not satisfied with this result, \nGenerate again.", Toast.LENGTH_SHORT).show()
                })
            Spacer(modifier = Modifier.height(20.dp))
            // Displaying the generated story or a default message.
            textResult?.let {
                Text(text = it, color = Color.Black)
            }
        }
    }
}