package com.sujoy.hernavigator.Presentation.Screen.SignUpIn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sujoy.hernavigator.Api.api.auth_api.RetrofitInstance
import com.sujoy.hernavigator.Api.data.Pref.UserPreferences
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.R
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.playFairDisplay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@Composable
fun SignInUi(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userPreferences: UserPreferences
) {
    val context= LocalContext.current
    val coroutineScope= rememberCoroutineScope()
    val passwordVisible = remember { mutableStateOf(false) }

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Image(
            painter = painterResource(id = R.drawable.g3),
            contentDescription = "bg3",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
        Image(
            painter = painterResource(id = R.drawable.g4),
            contentDescription = "bg4",
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .align(Alignment.TopEnd),
            contentScale = ContentScale.FillWidth
        )
//        Box(modifier = Modifier.fillMaxSize().background(color = Color(0x22646363)))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                fontSize = 35.sp,
                color = Color.Black,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Hey! Good to see you again",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = email.value, onValueChange = { email.value = it },
                placeholder = { Text(text = "Enter Email", color = Color.Gray) },
                label = { Text(text = "Email", color = Pink) },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Pink,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "email", tint = Pink)},
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text(text = "Enter Password", color = Color.Gray) },
                label = { Text(text = "Password", color = Pink) },
                colors = TextFieldDefaults.colors(
                    cursorColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Pink,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "password", tint = Pink)
                },
                trailingIcon = {
                    val image = if (passwordVisible.value) R.drawable.eye else R.drawable.eye_crossed
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Image(painter = painterResource(id = image), contentDescription = if (passwordVisible.value) "Hide password" else "Show password",
                            modifier=Modifier.size(20.dp),
                            colorFilter = ColorFilter.tint(Color.Gray))
                    }
                },
                shape = RoundedCornerShape(corner = CornerSize(10.dp)),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                coroutineScope.launch {
                    try {
                        val response = RetrofitInstance.api.signIn(email.value, password.value)
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val userId = response.body()?.message ?: ""
                                userPreferences.saveUserId(userId)

                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                navController.navigate(Routes.Home) {
                                    popUpTo(Routes.Login) { inclusive = true }  // Clear back stack
                                }
                            } else {
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = try {
                                    JSONObject(errorBody ?: "").getString("message")
                                } catch (e: Exception) {
                                    "Login Failed! Check credentials."
                                }
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
                modifier=Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(15.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                Text(text = "Login", fontSize = 17.sp, color = Pink, fontFamily = playFairDisplay, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account? ", fontSize = 12.sp, color = Color.White)
                Text(text = "SignUp", fontSize = 12.sp, color = Color.Green, fontWeight = FontWeight.Bold, modifier=Modifier.clickable { navController.navigate(Routes.SignUp) })
            }
        }
    }
}