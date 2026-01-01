package com.example.chatapp.view.chat

import android.R.attr.navigationIcon
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.constants.DialogType
import com.example.chatapp.model.GroupData
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.HashMap
import kotlin.collections.listOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
) {
    val auth = Firebase.auth
    var menuExpanded by remember { mutableStateOf(false) }
    var chatList by remember { mutableStateOf(listOf<GroupData>()) }

    val userData by viewModel.userData.collectAsState()

    chatViewModel.saveUsersList(userData)
    chatViewModel.saveUserChatGroups(userData)

    val userChatGroups by chatViewModel.userChatGroups.collectAsStateWithLifecycle()

    if (userChatGroups.isNotEmpty()) {
        val arr = userChatGroups.values
        val a1 = arr.filter { it.members[userData.uid]!!.active }
        val a2 = arr.filter { !it.members[userData.uid]!!.active }
        chatList = a1 + a2
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    // App is going to background
                    chatViewModel.updateOnlineOfflineStatus(userData.uid, false)
                }

                Lifecycle.Event.ON_START -> {
                    // App is coming to foreground
                    chatViewModel.updateOnlineOfflineStatus(userData.uid, true)
                }

                else -> { /* Do nothing for other events */
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ChatApp",
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .heightIn(max = 24.dp)
                    )
                },
                navigationIcon = {},
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "New Chat",
                            tint = colorResource(id = R.color.white)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(onClick = {
                            menuExpanded = false
                            navController.navigate(Screen.NewChatScreen.route)
                        }, text = { Text("New Chat") })
                        DropdownMenuItem(onClick = {
                            menuExpanded = false
                            navController.navigate(Screen.ProfileDetailScreen.route)
                        }, text = { Text("Profile") })
                        DropdownMenuItem(onClick = {
                            menuExpanded = false
                            auth.signOut()
                            viewModel.saveUserData(
                                UserData(
                                    "", "", "", "", false, "", 0
                                )
                            )
                            navController.navigate(Screen.LoginScreen.route)
                        }, text = { Text("Logout") })
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.purple_500)),
            )
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            items(chatList, key = { chatItem -> chatItem.groupId }) { chatItem ->
                ChatsListItem(chatItem = chatItem) {
                    val groupId = chatItem.groupId
                    val chatType =
                        if (chatItem.group) DialogType.GROUP_CHAT else DialogType.ONE_ON_ONE_CHAT
                    navController.navigate(Screen.ChatMessagesScreen.route + "/$groupId/${chatType}/ChatsScreen")
                }
            }
        }

    }
}

@Composable
fun ChatsListItem(chatItem: GroupData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            .clickable {
                onClick()
            }, elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = chatItem.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop, // Crop to fill the circular shape
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape) // Apply circular clip
            )
            Column(
                Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = chatItem.name, fontWeight = FontWeight.ExtraBold)
                chatItem.lastMessage?.let {
                    Text(
                        text = it.message,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}