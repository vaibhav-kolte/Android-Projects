package com.vkolte.musicappui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vkolte.musicappui.ui.theme.MusicAppUITheme

@Composable
fun BrowseScreen() {
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga", "Riding")
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(categories) { cat ->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .size(160.dp),
                border = BorderStroke(2.dp, color = Color.DarkGray)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = cat)
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = cat
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BrowsePreview() {
    MusicAppUITheme {
        BrowseScreen()
    }
}