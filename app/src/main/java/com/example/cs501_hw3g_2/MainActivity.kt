package com.example.cs501_hw3g_2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs501_hw3g_2.ui.theme.CS501hw3g2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS501hw3g2Theme {
            ReminderApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderApp() {
    val context = LocalContext.current
    var reminderMessage by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var isReminderSet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope= rememberCoroutineScope()
    val calendar = Calendar.getInstance()

    // Date Picker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            calendar.set(year, month, dayOfMonth)
            selectedDate = sdf.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Time Picker Dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedTime = sdf.format(calendar.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },  // Use Scaffold to handle Snackbar
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Reminder Message Input
            TextField(
                value = reminderMessage,
                onValueChange = { reminderMessage = it },
                label = { Text("Reminder Message") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Select Date Button
            Button(onClick = { datePickerDialog.show() }) {
                Text(text = if (selectedDate.isEmpty()) "Select Date" else selectedDate)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Select Time Button
            Button(onClick = { timePickerDialog.show() }) {
                Text(text = if (selectedTime.isEmpty()) "Select Time" else selectedTime)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Set Reminder Button
            Button(
                onClick = {
                    if (reminderMessage.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                        isReminderSet = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Reminder set successfully!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set Reminder")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clear Reminder Button
            if (isReminderSet) {
                Button(
                    onClick = {
                        reminderMessage = ""
                        selectedDate = ""
                        selectedTime = ""
                        isReminderSet = false
                        scope.launch  {
                            snackbarHostState.showSnackbar("Reminder cleared!")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Reminder")
                }
            }

            // Display the reminder if set
            if (isReminderSet) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Reminder: $reminderMessage")
                Text("Date: $selectedDate")
                Text("Time: $selectedTime")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderAppPreview() {
    ReminderApp()
}