package com.sujoy.hernavigator.Presentation.Screen.SelfCare

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes
import com.sujoy.hernavigator.ViewModel.SelfCareViewModel
import com.sujoy.hernavigator.ui.theme.Pink
import com.sujoy.hernavigator.ui.theme.indieFlower
import com.sujoy.hernavigator.ui.theme.playFairDisplay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskSelectionScreen(
    context: Context,
    viewModel: SelfCareViewModel,
    navController: NavHostController
) {
    val taskData by viewModel.tasks.observeAsState(emptyMap()) // Observe task data from ViewModel
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    androidx.compose.material3.Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Pink,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        "SelfCare Reminder",
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
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier
            .padding(innerPadding)
            .background(Color.White)) {
            items(taskData.keys.toList().size) { index ->
                val task = taskData.keys.toList()[index]
                val (time, duration) = taskData[task] ?: Pair("12:00 AM", 0)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(3.dp, Pink, shape = RoundedCornerShape(12.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        task,
                        fontFamily = indieFlower,
                        fontSize = 20.sp,
                        color = Pink,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Time Picker with AM/PM format
                    var selectedTime by remember { mutableStateOf(time) }
                    TimePickerFieldWithAmPm(
                        label = "Select Time for $task",
                        selectedTime = selectedTime,
                        onTimeSelected = { selectedTime = it }
                    )

                    // Duration Input
                    var selectedDuration by remember { mutableStateOf(duration.toString()) }
                    DurationField(
                        label = "Duration (in minutes)",
                        selectedDuration = selectedDuration,
                        onDurationChanged = { selectedDuration = it }
                    )

                    Button(
                        onClick = {
                            // Check if selectedTime is in the "hh:mm AM/PM" format
                            val timeFormatRegex = Regex("^(0[1-9]|1[0-2]):[0-5][0-9] [AP]M$")

                            if (!timeFormatRegex.matches(selectedTime)) {
                                // Show toast if the format is incorrect
                                Toast.makeText(
                                    context,
                                    "Please enter time in the format 'hh:mm AM/PM'.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                // Save task to ViewModel
                                viewModel.saveTask(task, selectedTime, selectedDuration.toInt())

                                // Show toast for confirmation
                                Toast.makeText(context, "$task reminder set!", Toast.LENGTH_SHORT).show()

                                // Schedule notifications
                                scheduleReminder(context, task, selectedTime, selectedDuration.toInt())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Pink),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Remind for $task", color = Color.White, fontSize = 18.sp)
                    }

                    Divider()
                }
            }
        }
    }
}

@Composable
fun TimePickerFieldWithAmPm(
    label: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    OutlinedTextField(
        value = selectedTime, onValueChange = { onTimeSelected(it) },
        label = { Text(label, color = Pink) },
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
        singleLine = true // Ensures input stays on a single line
    )
}

@Composable
fun DurationField(
    label: String,
    selectedDuration: String,
    onDurationChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = selectedDuration, onValueChange = { onDurationChanged(it) },
        label = { Text(label, color = Pink) },
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
}