package com.vkolte.locationapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vkolte.locationapp.ui.theme.LocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: LocationViewModel = viewModel()
            LocationAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModel)
                }
            }
        }
    }

}

@Composable
fun MyApp(viewModel: LocationViewModel) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(locationUtils = locationUtils, viewModel, context = context)
}


@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
) {

    val location = viewModel.location.value

    val address = location?.let { locationUtils.reverseGeocodeLocation(context, location) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            // I have access to permission
            locationUtils.requestLocationUpdates(viewModel = viewModel)
        } else {
            // Ask for permission
            val rationalRequest = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (rationalRequest) {
                Toast.makeText(
                    context,
                    "Location permission is required for this feature to work",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Location permission is required. Please enabled it in the Android Settings",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (location != null) {
//            Text(text = "Address: ${location.latitude} ${location.longitude}")
            Text(
                text = "Address: $address \n\nLatitude: ${location.latitude} Longitude: ${location.longitude}",
                modifier = Modifier
                    .wrapContentSize()
                    .padding(4.dp)
            )
        } else {
            Text(text = "Location not available")
        }

        Button(onClick = {
            if (locationUtils.hasLocationPermission(context)) {
                // Location permission already granted
                Toast.makeText(
                    context,
                    "Permission granted",
                    Toast.LENGTH_LONG
                ).show()
                locationUtils.requestLocationUpdates(viewModel)
            } else {
                // Request location permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }) {

            Text(text = "Get Location")
        }
    }
}