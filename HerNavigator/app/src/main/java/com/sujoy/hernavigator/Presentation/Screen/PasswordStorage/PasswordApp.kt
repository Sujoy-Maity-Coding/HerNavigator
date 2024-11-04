package com.sujoy.hernavigator.Presentation.Screen.PasswordStorage

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.sujoy.hernavigator.Model.PasswordEntry
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ViewModel.PasswordViewModel
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordApp(viewModel: PasswordViewModel = viewModel(), navController: NavHostController) {
    var isAuthenticated by remember { mutableStateOf(false) }
    var enteredPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
//    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Pink,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        "Password Locker",
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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            LaunchedEffect(isAuthenticated) {
                if (isAuthenticated) {
                    // Ensures the UI navigates to MainScreen immediately upon successful authentication
                }
            }

            if (viewModel.isPasswordSet()) {
                // Show login screen if password is set
                if (isAuthenticated) {
                    MainScreen(viewModel)
                } else {
                    LoginScreen(
                        password = enteredPassword,
                        onPasswordChange = { enteredPassword = it },
                        onAuthenticate = {
                            if (viewModel.validatePassword(enteredPassword)) {
                                isAuthenticated = true
                            } else {
                                // Show toast if password is incorrect
                                Toast.makeText(
                                    context,
                                    "Incorrect password. Try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            } else {
                // First-time password setup with immediate navigation upon completion
                SetPasswordScreen(viewModel::setAppPassword) {
                    isAuthenticated =
                        true // Immediately navigate to MainScreen after setting password
                }
            }
        }
    }
}




@Composable
fun LoginScreen(password: String, onPasswordChange: (String) -> Unit, onAuthenticate: () -> Unit) {
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
    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = password, onValueChange = onPasswordChange,
            label = { Text(text = "Enter password", color = Pink) },
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
        )
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.unlock).apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
            ),
            contentDescription = "Unlock",
            modifier = Modifier
                .size(70.dp)
                .clickable {
                    onAuthenticate()
                }
        )
    }
}

@Composable
fun SetPasswordScreen(onSetPassword: (String) -> Unit, onSuccess: () -> Unit) {
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text(text = "Enter new password", color = Pink) },
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
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (password.isNotEmpty()) {
                onSetPassword(password)
                onSuccess() // Navigate to MainScreen immediately after setting password
            }
        }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Pink)) {
            Text("Set Password", fontFamily = playFairDisplay, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MainScreen(viewModel: PasswordViewModel) {
    var service by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = service, onValueChange = { service = it },
            label = { Text(text = "Enter service", color = Pink) },
            placeholder = { Text(text = "e.g., Google, Facebook", color = Color.Gray) },
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
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text(text = "Enter Password", color = Pink) },
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
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (service.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.addPassword(service, password)
                    service = ""
                    password = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Pink)
        ) {
            Text("Add Password", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Saved Passwords:", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            PasswordList(viewModel.passwords, viewModel::deletePassword)
        }
    }
}


@Composable
fun PasswordList(passwords: List<PasswordEntry>, onDelete: (String) -> Unit) {
    LazyColumn {
        items(passwords.size) { index ->
            val entry = passwords[index]
            PasswordItem(entry, onDelete)
        }
    }
}

@Composable
fun PasswordItem(entry: PasswordEntry, onDelete: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(18.dp))
            .border(1.dp, Pink, shape = RoundedCornerShape(18.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(entry.service, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                Text(entry.password, fontSize = 16.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { onDelete(entry.service) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}