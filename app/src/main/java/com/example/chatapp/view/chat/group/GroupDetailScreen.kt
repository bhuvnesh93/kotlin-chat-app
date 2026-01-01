package com.example.chatapp.view.chat.group

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.model.GroupDetailMembersData
import com.example.chatapp.model.GroupMembers
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.view.chat.NewChatListItem
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel

@Composable
fun GroupDetailScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    groupId: String,
) {
    val userChatGroups by chatViewModel.userChatGroups.collectAsStateWithLifecycle()
    val userData by viewModel.userData.collectAsState()
    val userList by chatViewModel.userList.collectAsState()

    var groupName by remember { mutableStateOf("") }
    var groupMembersList: List<GroupDetailMembersData> by remember { mutableStateOf(emptyList()) }
    var totalMembers by remember { mutableIntStateOf(0) }
    var isSelfUserAdmin by remember { mutableStateOf(false) }
    var isSelfUserActive by remember { mutableStateOf(false) }

    LaunchedEffect(userChatGroups) {
        val groupDetail = userChatGroups[groupId];
        groupName = groupDetail!!.name
//        setGroupImage(groupDetail.image_url);
        if (groupDetail.members[userData.uid] != null) {
            isSelfUserAdmin = groupDetail.members[userData.uid]!!.admin as Boolean
            isSelfUserActive = groupDetail.members[userData.uid]!!.active
        }
    }

    LaunchedEffect(userChatGroups) {
        val groupDetail = userChatGroups[groupId]
        groupDetail!!.members.values.forEach { item ->
            if (item.active) {
                totalMembers += 1
                groupMembersList = if (userData.uid == item.uid) {
                    groupMembersList.toMutableList().apply {
                        add(
                            GroupDetailMembersData(
                                userData.uid,
                                "You",
                                userData.imageUrl,
                                item.admin
                            )
                        )
                    }
                } else {
                    val user = userList.find { it.uid == item.uid }
                    Log.e("user : ", user.toString())
                    groupMembersList.toMutableList().apply {
                        add(
                            GroupDetailMembersData(
                                user!!.uid,
                                user.name,
                                user.imageUrl,
                                item.admin
                            )
                        )
                    }
                }
            } else {
                totalMembers = totalMembers - 1
            }
        }
    }

    Log.e("groupMembersList : ", groupMembersList.toString())

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "Group Detail", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(groupName)
            Text("$totalMembers Members")
            if (isSelfUserAdmin && isSelfUserActive)
                Text("Add Member", modifier = Modifier.clickable {
                    navController.navigate(Screen.AddNewMemberScreen.route + "/$groupMembersList/$groupId")
                })
            if (groupMembersList.isNotEmpty())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(groupMembersList, key = { item -> item.uid }) { item ->
                        GroupDetailMembersListItem(
                            viewModel,
                            chatViewModel,
                            item = item,
                            groupId,
                            onClick = {
                                //
                            })
                    }
                }
        }
    }
}

@Composable
fun GroupDetailMembersListItem(
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    item: GroupDetailMembersData,
    groupId: String,
    onClick: () -> Unit,
) {

    var isOverFlowShown by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val userData by viewModel.userData.collectAsState()
    val userChatGroups by chatViewModel.userChatGroups.collectAsStateWithLifecycle()

    LaunchedEffect(userChatGroups) {
        val groupDetail = userChatGroups[groupId];
        isOverFlowShown = (groupDetail?.members[userData.uid]!!.admin == true
                && groupDetail.members[userData.uid]!!.active
                && item.uid != userData.uid)
    }

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
            Row(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                }
            }
            if (item.admin == true)
                Text("Admin")
            if (isOverFlowShown && !item.admin!!) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = colorResource(id = R.color.black)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(onClick = {
                        menuExpanded = false
                        //
                    }, text = { Text("Remove") })
                    DropdownMenuItem(onClick = {
                        menuExpanded = false
                        //
                    }, text = { Text("Make Admin") })

                }
            } else if (isOverFlowShown && item.admin!!) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = colorResource(id = R.color.black)
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(onClick = {
                        menuExpanded = false
                        //
                    }, text = { Text("Remove") })
                    DropdownMenuItem(onClick = {
                        menuExpanded = false
                        //
                    }, text = { Text("Remove Admin") })
                }
            }
        }
    }
}