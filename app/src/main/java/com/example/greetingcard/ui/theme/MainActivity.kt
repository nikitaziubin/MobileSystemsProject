package com.example.greetingcard.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.greetingcard.viewmodel.LocationViewModel

class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

//        Thread{
//            val db = AppDatabase.getDataBase(this)
//            db.clearAllTables()
//        }.start()

        val viewModel = LocationViewModel(application)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SimpleApp(viewModel)
                }
            }
        }
    }

    @Composable
    fun SimpleApp(viewModel: LocationViewModel){
        var isTableScreen by remember { mutableStateOf(true) }
        if(isTableScreen){
            TableScreen(viewModel = viewModel, onNavigate = { isTableScreen = false})
        }else{
            DynamicListScreen(viewModel = viewModel, onNavigate = { isTableScreen = true})
        }
    }
}