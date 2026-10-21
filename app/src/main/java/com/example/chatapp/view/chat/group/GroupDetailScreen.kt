package com.example.chatapp.view.chat.group

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.model.GroupData
import com.example.chatapp.model.GroupDetailMembersData
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import com.google.gson.Gson

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
    var groupImage by remember { mutableStateOf("") }
    var groupMembersList: List<GroupDetailMembersData> by remember { mutableStateOf(emptyList()) }
    var totalMembers by remember { mutableIntStateOf(0) }
    var isSelfUserAdmin by remember { mutableStateOf(false) }
    var isSelfUserActive by remember { mutableStateOf(false) }

    LaunchedEffect(userChatGroups) {
        val groupDetail = userChatGroups[groupId];
        groupName = groupDetail!!.name
        groupImage = groupDetail.imageUrl
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
                totalMembers -= 1
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    placeholder = painterResource(R.drawable.ic_user_placeholder),
                    error = painterResource(R.drawable.ic_user_placeholder),
                    model = groupImage,
                    contentDescription = "",
                    contentScale = ContentScale.Crop, // Crop to fill the circular shape
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape) // Apply circular clip
                )
                Text(
                    groupName,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Text(
                "$totalMembers Members",
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(10.dp)
//                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
            ) {
                if (isSelfUserAdmin && isSelfUserActive)
                    Text(
                        "Add Member",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .clickable {
                                val serializedObjects = Gson().toJson(groupMembersList)
                                navController.navigate(Screen.AddNewMemberScreen.route + "/$serializedObjects/$groupId")
                            })
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                if (groupMembersList.isNotEmpty())
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(groupMembersList, key = { item -> item.uid }) { item ->
                            GroupDetailMembersListItem(
                                item = item,
                                groupId,
                                userChatGroups,
                                userData,
                                onClick = {
                                    //
                                })
                        }
                    }
            }
        }
    }
}

@Composable
fun GroupDetailMembersListItem(
    item: GroupDetailMembersData,
    groupId: String,
    userChatGroups: Map<String, GroupData> = emptyMap(),
    userData: UserData = UserData(),
    onClick: () -> Unit,
) {
    var isOverFlowShown by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(userChatGroups) {
        val groupDetail = userChatGroups[groupId];
        isOverFlowShown = (groupDetail?.members[userData.uid]!!.admin == true
                && groupDetail.members[userData.uid]!!.active
                && item.uid != userData.uid)
    }

    Column() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(5.dp),
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(start = 10.dp)
                )
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
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = colorResource(id = R.color.black)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.wrapContentWidth()
                    ) {
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
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun GroupDetailMembersListItemPreview() {
    GroupDetailMembersListItem(
        item = GroupDetailMembersData(
            "123",
            "Bhuvneshwar Garg",
            "",
            true
        ),
        groupId = "123",
        onClick = {}
    )
}