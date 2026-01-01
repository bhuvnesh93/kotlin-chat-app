package com.example.chatapp.view.chat.group

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.chatapp.model.UserData
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel

@Composable
fun AddNewMemberScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    addedMembers: List<UserData>,
    groupId: String,
) {

    Log.e("addedMembers : ", addedMembers.toString())
    Log.e("groupId : ", groupId)

    val userList by chatViewModel.userList.collectAsState()
    val userData by viewModel.userData.collectAsState()

    var selectedMembers by remember { mutableStateOf(emptyList<UserData>()) }
}