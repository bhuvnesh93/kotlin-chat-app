package com.example.chatapp.view.chat.group

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.components.AppBarView
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.view.chat.components.NewChatListItem
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import com.google.gson.Gson

@Composable
fun AddNewMemberScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    addedMembers: List<UserData>,
    groupId: String,
) {
    val userList by chatViewModel.userList.collectAsState()
    val userData by viewModel.userData.collectAsState()

    var selectedMembers by remember { mutableStateOf(emptyList<UserData>()) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "Add New Contact", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(userList, key = { item -> item.uid }) { item ->
                    NewChatListItem(item = item, onClick = {
                        selectedMembers = if (selectedMembers.contains(item)) {
                            selectedMembers - item
                        } else {
                            selectedMembers + item
                        }
                    })
                }
            }
            if (selectedMembers.isNotEmpty())
                Button(
                    onClick = {
                        val serializedObjects = Gson().toJson(selectedMembers)
                        navController.navigate(Screen.CreateGroupScreen.route + "/$serializedObjects")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                ) {
                    Text("Continue")
                }
        }
    }
}