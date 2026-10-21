package com.example.greetingcard.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.greetingcard.viewmodel.LocationViewModel

val DarkGrey = Color(0xFF444444)

@Composable
fun TableScreen(viewModel: LocationViewModel, onNavigate: () -> Unit){
    val signalMap by viewModel.signalMap.collectAsState()
    val users by viewModel.users.collectAsState()
    val mapSize by viewModel.mapSize.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = onNavigate,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Text("Go to Signal List")
        }
        Text("Measurement Point Grid (Map)", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val currentMapSize = mapSize
        LazyColumn(modifier = Modifier.fillMaxSize().weight(1f)) {
            items((currentMapSize.minY ..currentMapSize.maxY).reversed().toList()){ rowIndex ->
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items((currentMapSize.minX..currentMapSize.maxX).toList()) { colIndex ->

                        val mapCell = signalMap.find { it.x == colIndex && it.y == rowIndex}
                        //val isUserHere = users.any {it.estimatedX == colIndex && it.estimatedY == rowIndex}

                        val cellText = when {
                          //  isUserHere -> "U"
                            mapCell != null -> "1"
                            else -> "0"
                        }
                        val cellBg = when {
                            //isUserHere -> Color.Red
                            mapCell != null -> Color.Green
                            else -> Color.LightGray
                        }

                        Text(
                            text = cellText,
                            color = Color.Black,
                            fontSize = 9.sp,
                            maxLines = 1,
                            modifier = Modifier
                                .width(27.9.dp)
                                .background(cellBg)
                                .border(0.5.dp, DarkGrey)
                                .padding(2.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

    }
}