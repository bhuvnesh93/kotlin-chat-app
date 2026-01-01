package com.example.chatapp.view.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.constants.DialogType
import com.example.chatapp.model.GroupData
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.utils.generatePrivateChatId
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
            Button(onClick = {
                navController.navigate(Screen.AddMemberScreen.route)
            }) {
                Text("New Group")
            }
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
fun NewChatListItem(item: UserData, onClick: () -> Unit) {
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
                placeholder = painterResource(R.drawable.ic_user_placeholder),
                error = painterResource(id = R.drawable.ic_user_placeholder),
                model = item.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop, // Crop to fill the circular shape
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape) // Apply circular clip
            )
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = item.name, fontWeight = FontWeight.ExtraBold, modifier = Modifier
                        .fillMaxWidth()
                )
                Text(text = item.email, fontWeight = FontWeight.Normal)
            }
        }
    }
}