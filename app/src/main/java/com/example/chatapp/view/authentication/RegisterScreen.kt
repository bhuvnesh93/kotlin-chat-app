package com.example.chatapp.view.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.navigation.Screen
import com.example.chatapp.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val auth = Firebase.auth
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(topBar = {
        AppBarView("Register", onBackNavClicked = { navController.popBackStack() })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(
                    scrollState
                )
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = {
                    Text(text = "Username", color = Color.Black)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = {
                    Text(text = "Password", color = Color.Black)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .semantics {
                        contentDescription = ""
                    },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val (icon, iconColor) = if (showPassword) {
                        Pair(
                            Icons.Filled.Visibility,
                            colorResource(id = R.color.black)
                        )
                    } else {
                        Pair(Icons.Filled.VisibilityOff, colorResource(id = R.color.black))
                    }
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            icon,
                            contentDescription = "Visibility",
                            tint = iconColor
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done, autoCorrectEnabled = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                label = {
                    Text(text = "Confirm Password", color = Color.Black)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .semantics {
                        contentDescription = ""
                    },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val (icon, iconColor) = if (showConfirmPassword) {
                        Pair(
                            Icons.Filled.Visibility,
                            colorResource(id = R.color.black)
                        )
                    } else {
                        Pair(Icons.Filled.VisibilityOff, colorResource(id = R.color.black))
                    }
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            icon,
                            contentDescription = "Visibility",
                            tint = iconColor
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done, autoCorrectEnabled = false
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black
                )
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            try {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Successful",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Authentication failed: ${task.exception?.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            } catch (e: Exception) {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "An error occurred: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    } else {
                        Toast.makeText(
                            context,
                            "Email and password cannot be empty.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Signup", style = TextStyle(fontSize = 15.sp))
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = {
                navController.popBackStack()
            }) {
                Text("Already have account? Login", style = TextStyle(fontSize = 15.sp))
            }
        }
    }
}