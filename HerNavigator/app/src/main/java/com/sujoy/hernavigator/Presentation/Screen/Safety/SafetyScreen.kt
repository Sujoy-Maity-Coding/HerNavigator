package com.sujoy.hernavigator.Presentation.Screen.Safety

import androidx.compose.runtime.Composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sujoy.hernavigator.ViewModel.SafetyViewModel
import com.sujoy.hernavigator.ui.theme.Pink

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.Presentation.Screen.Safety.Permission.RequestLocationPermission
import com.sujoy.hernavigator.Presentation.Screen.Safety.Permission.RequestSMSPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WomanSafetyScreen(viewModel: SafetyViewModel, navController: NavHostController) {
    var newContact by remember { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf<String?>(null) }
    val savedContacts =
        remember { mutableStateListOf<String>().apply { addAll(viewModel.getSavedContacts()) } }
    var isHelpButtonEnabled by remember { mutableStateOf(true) } // Controls button state
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Woman Safety",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Home){
                        popUpTo(navController.graph.startDestinationId) { inclusive = true } // Clears back stack up to start destination
                        launchSingleTop = true  // Prevents duplicate instances
                    }}) {
                        Icon(Icons.Default.Home, contentDescription = "home", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Pink)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display saved contacts with radio buttons to select one
            Text(
                text = "Select an Emergency Contact",
                fontSize = 20.sp,
                color = Pink
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (savedContacts.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, shape = RoundedCornerShape(12.dp))
                        .border(2.dp, Pink, shape = RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    items(savedContacts) { contact ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedContact == contact,
                                onClick = { selectedContact = contact },
                                colors = RadioButtonDefaults.colors(
                                    Pink,
                                    Pink
                                ),
                            )
                            Text(text = contact, modifier = Modifier.weight(1f), color = Color.Black)
                            Button(
                                onClick = {
                                    viewModel.removeContact(contact)
                                    savedContacts.remove(contact)
                                    Toast.makeText(context, "Contact removed", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                colors = ButtonDefaults.buttonColors(Pink)
                            ) {
                                Text("Remove", color = Color.White)
                            }
                        }
                    }
                }
            } else {
                Text(text = "No contacts saved yet.", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = newContact, onValueChange = { newContact = it },
                label = { Text(text = "Enter Phone no.", color = Pink) },
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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if ((newContact.length==10) && (newContact.all { it.isDigit() })) {
                        if (newContact.isNotEmpty() && savedContacts.size < 5) {
                            viewModel.addContact(newContact)
                            savedContacts.add(newContact)
                            newContact = ""
                            Toast.makeText(context, "Contact added", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Invalid contact or maximum contacts reached",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else{
                        Toast.makeText(context, "Enter Correct Phone no.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Text(text = "Add Contact", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Send Emergency Message Button
            Button(
                onClick = {
                    selectedContact?.let {
                        viewModel.sendEmergencyMessage(context, it) // Pass the context for location check

                        // Show Toast when sending SMS
                        Toast.makeText(context, "Attempting to send emergency message...", Toast.LENGTH_SHORT).show()
                    } ?: run {
                        // Show Toast if no contact selected
                        Toast.makeText(context, "Please select a contact first", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "HELP",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun WomanSafetyApp(safetyViewModel: SafetyViewModel, navController: NavHostController) {
    // Request location permission
    RequestLocationPermission {
        // Request SMS permission before showing the safety screen
        RequestSMSPermission {
            WomanSafetyScreen(viewModel = safetyViewModel, navController=navController)
        }
    }
}