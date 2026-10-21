package com.example.chatapp.view.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.components.AppImage
import com.example.chatapp.constants.DialogType
import com.example.chatapp.model.GroupData
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.utils.generatePrivateChatId
import com.example.chatapp.view.chat.components.NewChatListItem
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
) {
    val userData by viewModel.userData.collectAsState()
    val userList by chatViewModel.userList.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "New Chat", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            NewGroup(navController)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(userList, key = { item -> item.uid }) { item ->
                    NewChatListItem(item = item) {
                        val selfUserId = userData.uid
                        val otherUserId = item.uid
                        val groupId = generatePrivateChatId(selfUserId, otherUserId)
                        navController.navigate(Screen.ChatMessagesScreen.route + "/$groupId/${DialogType.ONE_ON_ONE_CHAT}/NewChatScreen") {
                            popUpTo(Screen.ChatsScreen.route)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewGroup(navController: NavController) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            .clickable {
                navController.navigate(Screen.AddMemberScreen.route)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // Set the overall size of the circular area
                .clip(CircleShape) // Clip the background to a circle shape
                .background(colorResource(R.color.teal_700)), // Apply the background color
            contentAlignment = Alignment.Center // Center the icon within the box
        ) {
            Icon(
                imageVector = Icons.Filled.GroupAdd,
                contentDescription = "profile",
                tint = Color.White, // Tint the icon color
                modifier = Modifier.size(30.dp) // Set the size of the icon itself
            )
        }
        Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
            Text(
                text = "New group",
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NewGroupPreview() {
    NewGroup(NavController(LocalContext.current))
}