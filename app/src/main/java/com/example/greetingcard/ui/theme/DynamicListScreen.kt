package com.example.greetingcard.ui.theme

import androidx.compose.foundation.clickable
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

    var macInput by remember { mutableStateOf("01:23:45:67:89:AB") }
    var strength1Input by remember { mutableStateOf("1") }
    var strength2Input by remember { mutableStateOf("2") }
    var strength3Input by remember { mutableStateOf("3") }

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
            value = strength1Input,
            onValueChange = { strength1Input = it },
            label = { Text("Strength 1 input") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = strength2Input,
            onValueChange = { strength2Input = it },
            label = { Text("Strength 2 input") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = strength3Input,
            onValueChange = { strength3Input = it },
            label = { Text("Strength 3 input") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        )

        Button(
            onClick = {
                if (macInput.isNotBlank()) {
                    viewModel.addOrUpdateUserLocation(
                        mac = macInput,
                        strength1 = strength1Input.toInt(),
                        strength2 = strength2Input.toInt(),
                        strength3 = strength3Input.toInt(),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Calculate Location and Save")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Signal strengths", style = MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items(users) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable{
                            viewModel.calculateAndHighlightLocation(user)
                            onNavigate()
                        }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "MAC: ${user.mac}", color = Color.Black)
                        Text(text = "Strength1: ${user.strength1} | Strength2: ${user.strength2} | Strength3: ${user.strength3}", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}