package com.example.chatapp.view.chat.group

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.constants.DialogType
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun CreateGroupScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    users: List<UserData>
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var groupName by remember { mutableStateOf("") }

    val userData by viewModel.userData.collectAsState()

    val context = LocalContext.current

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    fun createGroup() {
        if (groupName.isEmpty()) {
            Toast.makeText(context, "Please enter group name", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedImageUri != null) {
            chatViewModel.uploadImage(selectedImageUri!!, "group") { imageUrl ->
                chatViewModel.createGroup(groupName, userData.uid, users, imageUrl)
                { groupId ->
                    navController.navigate(Screen.ChatMessagesScreen.route + "/$groupId/${DialogType.GROUP_CHAT}/CreateGroupScreen") {
                        popUpTo(Screen.ChatsScreen.route)
                    }
                }
            }
            return
        }
        chatViewModel.createGroup(groupName, userData.uid, users, "")
        { groupId ->
            navController.navigate(Screen.ChatMessagesScreen.route + "/$groupId/${DialogType.GROUP_CHAT}/CreateGroupScreen") {
                popUpTo(Screen.ChatsScreen.route)
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "Create Group", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(100.dp) // Container size
                        .clickable {
                            pickMediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
//                    contentAlignment = Alignment.CenterVertically,
                    contentAlignment = Alignment.Center,
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            placeholder = painterResource(R.drawable.ic_user_placeholder),
                            error = painterResource(id = R.drawable.ic_user_placeholder),
                            model = selectedImageUri,
                            contentDescription = "",
                            contentScale = ContentScale.Crop, // Crop to fill the circular shape
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape) // Apply circular clip
                        )
                    } else {
                        AsyncImage(
                            placeholder = painterResource(R.drawable.ic_launcher_background),
                            error = painterResource(id = R.drawable.ic_launcher_background),
                            model = null,
                            contentDescription = "",
                            contentScale = ContentScale.Crop, // Crop to fill the circular shape
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape) // Apply circular clip
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                OutlinedTextField(
                    value = groupName, onValueChange = { groupName = it },
                    label = {
                        Text(text = "Enter group name", color = Color.Black)
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
            }
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                items(users) { item ->
                    Text(item.name)
                }
            }
            Button(onClick = {
                createGroup()
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Create Group")
            }
        }
    }
}
