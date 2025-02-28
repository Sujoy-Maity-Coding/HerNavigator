package com.sujoy.hernavigatoradmin.Presentation.Screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.sujoy.hernavigatoradmin.Presentation.Utils.compressImage
import com.sujoy.hernavigatoradmin.Presentation.Utils.uriToFile
import com.sujoy.hernavigatoradmin.Presentation.ViewModel.ApiViewModel
import com.sujoy.hernavigatoradmin.R
import com.sujoy.hernavigatoradmin.ui.theme.Pink
import java.io.File
import java.io.FileOutputStream

@Preview(showSystemUi = true)
@Composable
fun UploadImageScreen(viewModel: ApiViewModel= ApiViewModel()) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val uploadStatus by viewModel.uploadStatus.observeAsState("")

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Image Screen",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Pink,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 30.dp)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUri == null) {
                Card(
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Pink),
                    colors = CardDefaults.cardColors(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.add_image),
                            contentDescription = "upload",
                            modifier = Modifier.size(50.dp),
                            colorFilter = ColorFilter.tint(Color.Gray)
                        )
                    }
                }
            }

            imageUri?.let { uri ->
                Card(
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Pink),
                    colors = CardDefaults.cardColors(Color.Transparent)
                ) {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = {
                    val file = uriToFile(context, uri)
                    file?.let {
                        val compressedFile = compressImage(context, uri)
                        compressedFile?.let { viewModel.uploadFashionImage(it) }
                    } ?: Log.e("Upload", "Failed to convert URI to file")
                    // Clear the image URI after successful upload
                    imageUri=null
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Pink,
                        contentColor = Color.White
                    )) {
                    Text("Upload Image", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(7.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.LightGray)
                .height(2.dp))
            Text(text = "Upload Status :-", color = Color.Green, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(uploadStatus, color = Color.Black, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}