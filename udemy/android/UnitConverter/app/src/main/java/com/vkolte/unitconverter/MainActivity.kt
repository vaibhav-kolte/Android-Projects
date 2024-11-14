package com.vkolte.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkolte.unitconverter.ui.theme.UnitConverterTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverter()
                }
            }
        }
    }
}

@Composable
fun UnitConverter() {

    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }
    var iExpanded by remember { mutableStateOf(false) }
    var oExpanded by remember { mutableStateOf(false) }
    val iConversionFactor = remember { mutableDoubleStateOf(1.0) }
    val oConversionFactor = remember { mutableDoubleStateOf(1.0) }

    val customTextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 16.sp,
        color = Color.Red
    )


    fun convertUnits() {
        // ?: -> elvis operator
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result =
            (inputValueDouble * iConversionFactor.doubleValue * 100.0 / oConversionFactor.doubleValue).roundToInt() / 100.0
        outputValue = "$result $outputUnit"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Text(text = "Unit Converter", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Unit Converter", style = customTextStyle)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                // Here goes what should happen, when the value of our OutlinedTextFiled is changed
                inputValue = it
                convertUnits()
            },
            label = { Text(text = "Enter Value") }
        )
        // Here are all the UI elements will be stacked below each other
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            // Input Box
            Box {
                // Input button
                Button(onClick = { iExpanded = true }) {
                    Text(text = inputUnit)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = ""
                    )
                }

                DropdownMenu(expanded = iExpanded, onDismissRequest = { iExpanded = false }) {
                    DropdownMenuItem(text = { Text(text = "Centimeters") }, onClick = {
                        inputUnit = "Centimeters"
                        iExpanded = false
                        iConversionFactor.doubleValue = 0.01
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text(text = "Meters") }, onClick = {
                        inputUnit = "Meters"
                        iExpanded = false
                        iConversionFactor.doubleValue = 1.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text(text = "Feet") }, onClick = {
                        inputUnit = "Feet"
                        iExpanded = false
                        iConversionFactor.doubleValue = 0.3048
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text(text = "Millimeters") }, onClick = {
                        inputUnit = "Millimeters"
                        iExpanded = false
                        iConversionFactor.doubleValue = 0.001
                        convertUnits()
                    })
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Output Box
            Box {
                // Output Button
                Button(onClick = { oExpanded = true }) {
                    Text(text = outputUnit)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = ""
                    )
                }
                DropdownMenu(expanded = oExpanded, onDismissRequest = { oExpanded = false }) {
                    DropdownMenuItem(text = { Text(text = "Centimeters") }, onClick = {
                        outputUnit = "Centimeters"
                        oExpanded = false
                        oConversionFactor.doubleValue = 0.01
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text(text = "Meters") }, onClick = {
                        outputUnit = "Meters"
                        oExpanded = false
                        oConversionFactor.doubleValue = 1.0
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text(text = "Feet") }, onClick = {
                        outputUnit = "Feet"
                        oExpanded = false
                        oConversionFactor.doubleValue = 0.3048
                        convertUnits()
                    })
                    DropdownMenuItem(text = { Text(text = "Millimeters") }, onClick = {
                        outputUnit = "Millimeters"
                        oExpanded = false
                        oConversionFactor.doubleValue = 0.001
                        convertUnits()
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Result Text
        Text(
            text = "Results: $outputValue",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverterTheme {
        UnitConverter()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnitConverterTheme {
        Greeting("Android")
    }
}