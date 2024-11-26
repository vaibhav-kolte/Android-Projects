package com.vkolte.pickupdropservice

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vkolte.pickupdropservice.ui.theme.PickupDropServiceTheme

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val isAdminLogin = remember { mutableStateOf(false) }
    val isSignIn = remember { mutableStateOf(false) }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .size(if (isSignIn.value) 400.dp else 300.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Center content inside the Box
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // Center text and image horizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when {
                            isAdminLogin.value -> "Admin Login"
                            isSignIn.value -> "User SignIn"
                            else -> "User Login"
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = username.value, onValueChange = {
                        username.value = it
                    }, label = {
                        Text(text = "Username")
                    })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                        },
                        label = {
                            Text(text = "Password")
                        },
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible.value)
                                painterResource(id = R.drawable.ic_visibility)
                            else
                                painterResource(id = R.drawable.ic_visibility_off)

                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(
                                    painter = image,
                                    contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                                )
                            }
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isSignIn.value) {
                        OutlinedTextField(value = confirmPassword.value, onValueChange = {
                            confirmPassword.value = it
                        }, label = {
                            Text(text = "Confirm Password")
                        },
                            visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (confirmPasswordVisible.value)
                                    painterResource(id = R.drawable.ic_visibility)
                                else
                                    painterResource(id = R.drawable.ic_visibility_off)

                                IconButton(onClick = {
                                    confirmPasswordVisible.value = !confirmPasswordVisible.value
                                }) {
                                    Icon(
                                        painter = image,
                                        contentDescription = if (confirmPasswordVisible.value) "Hide password" else "Show password"
                                    )
                                }
                            },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Button(onClick = {
                        if (isSignIn.value) {
                            // SignIn logic
                            if (password.value == confirmPassword.value
                                && password.value.isNotEmpty() && username.value.isNotEmpty()
                            ) {
                                Toast.makeText(
                                    context,
                                    "Username ${username.value}\nPassword ${password.value}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Fill proper credentials",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            // Login logic
                            if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Username ${username.value}\nPassword ${password.value}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(context, "Fill credentials", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }) {
                        Text(text = if (isSignIn.value) "SignIn" else "Login")
                    }
                    if (!isAdminLogin.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            TextButton(onClick = {
                                isSignIn.value = !isSignIn.value
                            }) {
                                Text(text = if (isSignIn.value) "Login" else "Sign In")
                            }
                        }
                    }

                }
            }

        }
        Box(modifier = Modifier.padding(end = 16.dp)) {
            TextButton(onClick = {
                isSignIn.value = false
                isAdminLogin.value = !isAdminLogin.value
            }) {
                Text(text = if (isAdminLogin.value) "User Login" else "Admin Login")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PickupDropServiceTheme {
        LoginScreen()
    }
}