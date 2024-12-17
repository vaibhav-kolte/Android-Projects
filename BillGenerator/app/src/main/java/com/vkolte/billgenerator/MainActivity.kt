package com.vkolte.billgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vkolte.billgenerator.ui.theme.BillGeneratorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillGeneratorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BillGeneratorApp()
                }
            }
        }
    }
}

@Composable
private fun BillGeneratorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        
        composable("new_patient") {
            PrescriptionForm(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("recheck") {
            RecheckScreen(
                onNavigateToNewPrescription = { patientId ->
                    navController.navigate("new_prescription/$patientId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = "new_prescription/{patientId}",
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getLong("patientId") ?: return@composable
            PrescriptionForm(
                patientId = patientId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}