package com.vkolte.doorlocker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkolte.doorlocker.R

@Composable
fun AppLogo() {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Logo",
        modifier = Modifier.size(150.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun AppTitle() {
    Text(
        text = "VIDEO DOORBELL\nAPPLICATION!!",
        style = MaterialTheme.typography.titleMedium.copy(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center
    )
} 