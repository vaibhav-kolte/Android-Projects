package com.vkolte.captaingame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vkolte.captaingame.ui.theme.CaptainGameTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaptainGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CaptainGame()
                }
            }
        }
    }

    @Composable
    fun CaptainGame() {
        val treasuresFound = remember { mutableIntStateOf(0) }

//        val direction = remember { mutableStateOf("North") }
        var direction by remember { mutableStateOf("North") }
        val stormOrTreasure = remember { mutableStateOf("") }

        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Treasure Found: ${treasuresFound.intValue}")
            Text(text = "Current Direction: $direction")
            Text(text = stormOrTreasure.value)
            Button(onClick = {
                direction = "East"
                if (Random.nextBoolean()) {
                    treasuresFound.intValue += 1
                    stormOrTreasure.value = "Found a Treasure!"
                } else {
                    stormOrTreasure.value = "Storm Ahead!"
                }
            }) {
                Text(text = "Sail East")
            }
            Button(onClick = {
                direction = "West"
                if (Random.nextBoolean()) {
                    treasuresFound.intValue += 1
                    stormOrTreasure.value = "Found a Treasure!"
                } else {
                    stormOrTreasure.value = "Storm Ahead!"
                }
            }) {
                Text(text = "Sail West")
            }
            Button(onClick = {
//                direction.value = "North"
                direction = "North"
                if (Random.nextBoolean()) {
                    treasuresFound.intValue += 1
                    stormOrTreasure.value = "Found a Treasure!"
                } else {
                    stormOrTreasure.value = "Storm Ahead!"
                }
            }) {
                Text(text = "Sail North")
            }
            Button(onClick = {
                direction = "South"
                if (Random.nextBoolean()) {
                    treasuresFound.intValue += 1
                    stormOrTreasure.value = "Found a Treasure!"
                } else {
                    stormOrTreasure.value = "Storm Ahead!"
                }
            }) {
                Text(text = "Sail South")
            }
        }
    }

}
