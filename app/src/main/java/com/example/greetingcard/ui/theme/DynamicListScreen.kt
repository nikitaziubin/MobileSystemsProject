package com.example.greetingcard.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.viewmodel.LocationViewModel

@Composable
fun DynamicListScreen(viewModel: LocationViewModel, onNavigate: () -> Unit) {
    val users by viewModel.users.collectAsState()

    var macInput by remember { mutableStateOf("") }
    var sensorInput by remember { mutableStateOf("seklys_drone") }
    var strengthInput by remember { mutableStateOf("-55") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onNavigate, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Map Grid Page")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Add Device Signal & Determine Location", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = macInput,
            onValueChange = { macInput = it },
            label = { Text("MAC Address") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = sensorInput,
            onValueChange = { sensorInput = it },
            label = { Text("Access Point / Sensor Name") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = strengthInput,
            onValueChange = { strengthInput = it },
            label = { Text("Signal Strength (dBm)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Button(
            onClick = {
                if (macInput.isNotBlank()) {
                    viewModel.addOrUpdateUserLocation(
                        mac = macInput,
                        sensor = sensorInput,
                        strength = strengthInput.toIntOrNull() ?: -60
                    )
                    macInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Calculate Location and Save")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Registered Users / Signal Records (vartotojai)", style = MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "MAC: ${user.mac}", color = Color.Black)
                        Text(text = "Sensor: ${user.sensor} | Strength: ${user.stiprumas} dBm", fontSize = 12.sp)
                        Text(text = "Estimated Position: X = ${user.estimatedX}, Y = ${user.estimatedY}", fontSize = 12.sp, color = Color.Blue)
                    }
                }
            }
        }
    }
}