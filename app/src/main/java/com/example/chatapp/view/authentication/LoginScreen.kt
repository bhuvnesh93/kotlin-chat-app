package com.example.chatapp.view.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.utils.createOrUpdateUser
import com.example.chatapp.utils.uid
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import com.example.chatapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val auth = Firebase.auth
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(topBar = {
        AppBarView("Login")
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            ) {
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = {
                    Text(text = "Enter Username", color = Color.Black)
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
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                singleLine = true,
                label = {
                    Text(text = "Enter Password", color = Color.Black)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
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
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            try {
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            val uid = uid(task.result.user?.uid.toString())
                                            val name =
                                                task.result.user?.displayName.toString()
                                            val email =
                                                task.result.user?.email.toString()
                                            val userData = UserData(
                                                uid,
                                                name,
                                                "",
                                                "",
                                                true,
                                                email,
                                                0
                                            )
                                            createOrUpdateUser(userData) {
                                                viewModel.saveUserData(
                                                    userData
                                                )
                                                navController.navigate(Screen.ChatsScreen.route)
                                            }
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
                    Text("Login", style = TextStyle(fontSize = 15.sp))
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = {
                navController.navigate(Screen.RegisterScreen.route)
            }) {
                Text("Don't have account? Sign Up", style = TextStyle(fontSize = 18.sp))
            }
        }
    }
}
