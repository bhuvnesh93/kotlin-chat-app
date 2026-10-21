package com.example.chatapp.view.chat.group

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.components.AppImage
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.view.chat.components.NewChatListItem
import com.example.chatapp.view.chat.components.SelectedMemberListItem
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import com.google.gson.Gson

@Composable
fun AddMemberScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
) {
    val userList by chatViewModel.userList.collectAsState()

    var selectedMembers by remember { mutableStateOf(emptyList<UserData>()) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "Select Contact", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (selectedMembers.isNotEmpty())
//                SelectedMembersView(selectedMembers = selectedMembers, onDelete = {
//                    selectedMembers = selectedMembers - it
//                })
                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .height(100.dp) // Give LazyRow a fixed height
//                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    items(selectedMembers) { item ->
                        SelectedMemberListItem(selectedMember = item, onDelete = {
                            selectedMembers = selectedMembers - item
                        })
                    }
                }
            if (selectedMembers.isNotEmpty())
                Spacer(modifier = Modifier.height(16.dp))
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
