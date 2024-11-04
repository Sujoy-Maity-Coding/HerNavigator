package com.sujoy.hernavigator.Presentation.Util

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.Model.HomeData
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.SafetyViewModel
import com.sujoy.hernavigator.ui.theme.FadeBlue
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@Composable
fun DrawerContent(
    navController: NavController,
    imageList: List<HomeData>,
    modifier: Modifier = Modifier,
    context: Context,
    safetyViewModel: SafetyViewModel,
    context2: Context,
    imageLoader: ImageLoader
) {
    // Drawer content with padding and alignment
    Column(
        modifier = modifier
            .background(
                Color.Transparent,
                shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp)
            )
    ) {
        // Profile section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.lg),
                contentDescription = "HerNavigator",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "HerNavigator",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFAD1457)
            )
            Text(
                text = "Version: 1.0",
                fontSize = 14.sp,
                fontFamily = FontFamily.Serif,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clickable {
                navController.navigate(Routes.Home)
            }
            .padding(vertical = 8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context2).data(data = R.drawable.home_gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = "Home",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Home",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }
        var expanded by remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context2).data(data = R.drawable.tools_gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = "Tools",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Explore",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            imageList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.text) },
                    onClick = {
                        navController.navigate(item.route)
                        expanded = false
                    }
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clickable {
                safetyViewModel.sendEmergencyMessageToFirstContact(context = context)
            }
            .padding(vertical = 8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context2).data(data = R.drawable.location_gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = "Location",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Emergency",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }
        val showAboutDialog = remember { mutableStateOf(false) }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clickable {
                showAboutDialog.value = true
            }
            .padding(vertical = 8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context2).data(data = R.drawable.info_gif).apply(block = {
                        size(Size.ORIGINAL)
                    }).build(), imageLoader = imageLoader
                ),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = "Info",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "About",
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }

        if (showAboutDialog.value) {
            AlertDialog(
                onDismissRequest = { showAboutDialog.value = false },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog.value = false }) {
                        Text("Close", color = Pink)
                    }
                },
                title = {
                    Text(
                        text = "About HerNavigator",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Pink
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                    Text(
                        text = "HerNavigator was created on 27/10/2024 by Sujoy Maity and his dedicated team with a singular vision: to be the empowering companion every woman deserves.  This app was inspired by the tragic 2024 incident at Kolkata’s R G Kar Medical College, a reminder of the importance of prioritizing women’s safety and well-being. HerNavigator was designed to be more than just a tool—it’s a trusted companion that empowers, protects, and supports women every step of the way.\n" +
                                "\n" +
                                "A Mission for Empowerment and Care\n" +
                                "\n" +
                                "HerNavigator is crafted to support women on every front, from health and safety to style and budgeting. Whether it's staying connected with loved ones for peace of mind, setting and tracking wellness goals, or managing finances, each feature is thoughtfully designed to enhance life. Our team poured heart and innovation into this project, aiming to create an app that understands the challenges women face daily and offers real solutions.\n" +
                                "\n" +
                                "Why Women Love HerNavigator:\n" +
                                "\n" +
                                "Safety in Seconds: In moments of need, one tap lets your trusted contacts know exactly where you are, offering reassurance and support.\n" +
                                "Style at Your Fingertips: Whether you’re planning an outfit or seeking style advice, HerNavigator’s color-matching feature makes dressing up fun and easy.\n" +
                                "Health That Cares: From personalized diet reminders to gentle exercises for period comfort, we’ve got wellness covered.\n" +
                                "Financial Confidence: Manage your budget and daily expenses with clarity, helping you stay in control of your finances.\n" +"\n"+
                                "HerNavigator was built with purpose, commitment, and care, so every woman feels supported, safe, and empowered. Every day, every step—you can count on HerNavigator.",
                        fontFamily = playFairDisplay,
                        fontSize = 16.sp,
                        color = Color.Black
                    )}
                },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color.White
            )
        }

        // Add spacer to push the Help button to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Help button placed at the bottom
        Row(verticalAlignment = Alignment.CenterVertically) {
            val context = LocalContext.current
            val phoneNumber = "tel:7001925882"

            val wpNumber =
                "7001925882" // Replace with the target number in international format without '+'
            val message = "Hello, I need assistance with..."

            val callPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    // Permission granted, make the call
                    val intent = Intent(Intent.ACTION_CALL).apply {
                        data = Uri.parse(phoneNumber)
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No application found to make the call",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Call permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            var showDialog by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showDialog = true }) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context2).data(data = R.drawable.help_center).apply(block = {
                                size(Size.ORIGINAL)
                            }).build(), imageLoader = imageLoader
                        ),
                        colorFilter = ColorFilter.tint(Color.DarkGray),
                        contentDescription = "Help",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Customer Service",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(
                            text = "Welcome To Help Center !",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Pink
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "If you need assistance or have any questions, our customer service team is here to help! You can reach out to us by calling our support line or starting a live chat with one of our representatives. We're committed to providing you with the best support experience possible!",
                                fontFamily = playFairDisplay,
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        // Check for CALL_PHONE permission
                                        if (ContextCompat.checkSelfPermission(
                                                context, Manifest.permission.CALL_PHONE
                                            ) == PackageManager.PERMISSION_GRANTED
                                        ) {
                                            val intent = Intent(Intent.ACTION_CALL).apply {
                                                data = Uri.parse(phoneNumber)
                                            }
                                            try {
                                                context.startActivity(intent)
                                            } catch (e: ActivityNotFoundException) {
                                                Toast.makeText(
                                                    context,
                                                    "No application found to make the call",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        } else {
                                            callPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                    ),
                                    border = BorderStroke(2.dp, FadeBlue),
                                    modifier = Modifier.height(45.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.call),
                                            contentDescription = "call"
                                        )
                                        Text(
                                            text = "Call Us",
                                            color = FadeBlue,
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        val url =
                                            "https://wa.me/$wpNumber?text=${Uri.encode(message)}"
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                                        try {
                                            context.startActivity(intent)
                                        } catch (e: ActivityNotFoundException) {
                                            // WhatsApp is not installed, show a message to the user
                                            Toast.makeText(
                                                context,
                                                "WhatsApp is not installed on your device.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    },

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                    ),
                                    border = BorderStroke(2.dp, Color.Green),
                                    modifier = Modifier.height(45.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.chat),
                                            contentDescription = "chat"
                                        )
                                        Text(
                                            text = "Chat Us",
                                            color = Color.Green,
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = {},
                    shape = RoundedCornerShape(20.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}