package com.vkolte.musicappui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun HomeView() {
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")
    val grouped = listOf("New Release", "Favorites", "Top  Rated").groupBy { it[0] }
    LazyColumn {
        grouped.forEach { item ->
            stickyHeader {
                Text(text = item.value[0], modifier = Modifier.padding(16.dp))
                LazyRow {
                    items(categories) { cat ->
                        BrowserItem(cat = cat, drawable = R.drawable.music_video)
                    }
                }
            }
        }
    }
}

@Composable
fun BrowserItem(cat: String, drawable: Int) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(200.dp),
        border = BorderStroke(2.dp, color = Color.DarkGray)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = cat)
            Image(painter = painterResource(id = drawable), contentDescription = cat)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    MusicAppUITheme {
        HomeView()
    }
}
