package com.vkolte.billgenerater

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vkolte.billgenerater.ui.theme.BillGeneraterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillGeneraterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PrescriptionForm(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}