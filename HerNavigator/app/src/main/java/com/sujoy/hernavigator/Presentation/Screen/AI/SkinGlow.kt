package com.sujoy.hernavigator.Presentation.Screen.AI

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.HerNavigatorViewModel
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinGlow(
    modifier: Modifier = Modifier,
    viewModel: HerNavigatorViewModel,
    navController: NavHostController
) {
    val context= LocalContext.current
    val contentResolver = LocalContext.current.contentResolver
    val mimeTypeFilter = arrayOf("image/jpeg", "image/png")
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (Build.VERSION.SDK_INT < 28) {
            imageBitmap.value = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(contentResolver, uri!!)
            imageBitmap.value = ImageDecoder.decodeBitmap(source)
        }
    }

    val textResult = viewModel.skinGlowResult.collectAsState().value

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
                        "Glow Your Skin",
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
            if (imageBitmap.value != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp, top = 20.dp)
                        .height(300.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .border(
                            width = 3.dp,
                            color = Pink,
                            shape = RoundedCornerShape(30.dp)
                        ),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(30.dp)
                ) {
                    Image(
                        painter = BitmapPainter(
                            image = imageBitmap.value!!.asImageBitmap()
                        ), contentDescription = "Image",modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                    )
                }
            } else {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, end = 40.dp, top = 20.dp)
                        .height(300.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .border(
                            width = 3.dp,
                            color = Pink,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable {
                            imagePickerLauncher.launch(mimeTypeFilter)
                        },
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(30.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Image(painter = painterResource(id = R.drawable.face), contentDescription = "Face",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth())
                        Text(text = "Enter Your Face", fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, color = Color.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Image(painter = painterResource(id = R.drawable.sent), contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        imageBitmap.value?.let { viewModel.skinGlowGenerator(it) }
                        Toast.makeText(context, "If you're not satisfied with this result, \nGenerate again.", Toast.LENGTH_SHORT).show()
                    })
            Spacer(modifier = Modifier.height(20.dp))
            // Displaying the generated story or a default message.
            textResult?.let {
                Text(text = it, color = Color.Black)
            }
        }
    }
}