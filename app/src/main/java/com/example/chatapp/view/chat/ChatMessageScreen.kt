package com.example.chatapp.view.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.constants.DialogType
import com.example.chatapp.constants.MessageTypes
import com.example.chatapp.model.MessageData
import com.example.chatapp.model.UserData
import com.example.chatapp.navigation.Screen
import com.example.chatapp.utils.createOrUpdateUser
import com.example.chatapp.utils.uid
import com.example.chatapp.view.chat.chatviews.GroupNotificationView
import com.example.chatapp.view.chat.chatviews.IncomingChatView
import com.example.chatapp.view.chat.chatviews.OutgoingChatView
import com.example.chatapp.view.chat.components.ChatMessageBox
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatMessageScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    groupId: String,
    chatType: String,
    fromRoute: String,
) {
    var isGroupExist by remember { mutableStateOf(false) }
    var isSelfUserActive by remember { mutableStateOf(false) }
    var deleteTill by remember { mutableLongStateOf(0) }
    var lastSeenMessageTimeStamp by remember { mutableLongStateOf(0L) }
    var headerTitle by remember { mutableStateOf("") }
    var headerIcon by remember { mutableStateOf("") }
    var headerSubTitle by remember { mutableStateOf("") }


    val messageList by chatViewModel.chatMessages.collectAsState()
    Log.e("messageList ; ", messageList.toString())

    val userData by viewModel.userData.collectAsState()
    val userList by chatViewModel.userList.collectAsStateWithLifecycle()
    val userChatGroups by chatViewModel.userChatGroups.collectAsStateWithLifecycle()

    LaunchedEffect(userChatGroups) {
        isGroupExist = groupId in userChatGroups
        if (isGroupExist) {
            isSelfUserActive = userChatGroups[groupId]!!.members[userData.uid]!!.active
            deleteTill = userChatGroups[groupId]!!.members[userData.uid]!!.deleteTill
            lastSeenMessageTimeStamp =
                userChatGroups[groupId]!!.members[userData.uid]!!.lastSeenMessageTimestamp
        }
    }

    LaunchedEffect(deleteTill) {
        chatViewModel.fetchMessagesByGroup(groupId, deleteTill)
    }

    LaunchedEffect(Unit) {
        if (chatType == DialogType.ONE_ON_ONE_CHAT) {
            val otherUserId = groupId.split("_").find {
                it != userData.uid
            }
            val otherUserDetail = userList.find { it ->
                it.uid == otherUserId
            }
            headerTitle = otherUserDetail!!.name
            headerIcon = otherUserDetail.imageUrl
            headerSubTitle = if (otherUserDetail.online) {
                "Online"
            } else {
                "Last seen"
            }
        } else {
            headerTitle = userChatGroups[groupId]!!.name
            headerIcon = userChatGroups[groupId]!!.imageUrl
            headerSubTitle = userChatGroups[groupId]!!.members.size.toString() + " Members"
        }
    }

    Log.e("groupId", groupId)
    Log.e("chatType", chatType)
    Log.e("fromRoute", fromRoute)

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            if (chatType == DialogType.ONE_ON_ONE_CHAT) {
                                val otherUserId = groupId.split("_").find {
                                    it != userData.uid
                                }
                                navController.navigate(Screen.OtherUserProfileScreen.route + "/${otherUserId}")
                            } else
                                navController.navigate(Screen.GroupDetailScreen.route + "/$groupId")
                        }
                    ) {
                        AsyncImage(
                            placeholder = if (chatType == DialogType.ONE_ON_ONE_CHAT) painterResource(
                                R.drawable.ic_user_placeholder
                            ) else painterResource(R.drawable.ic_user_placeholder),
                            error = if (chatType == DialogType.ONE_ON_ONE_CHAT) painterResource(
                                R.drawable.ic_user_placeholder
                            ) else painterResource(R.drawable.ic_user_placeholder),
                            model = headerIcon,
                            contentDescription = "",
                            contentScale = ContentScale.Crop, // Crop to fill the circular shape
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape) // Apply circular clip
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                headerTitle,
                                color = colorResource(id = R.color.white),
                                style = TextStyle(fontSize = 16.sp)
                            )
                            Text(
                                headerSubTitle,
                                modifier = Modifier.padding(top = 4.dp),
                                style = TextStyle(fontSize = 13.sp, color = Color.White)
                            )
                        }

                    }
                },
                navigationIcon = {
                    IconButton({ navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "menu items",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(colorResource(R.color.teal_700)),
            )
        }) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                reverseLayout = true, // This is the key parameter
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                messageList.forEach { (date, section) ->

                    // Add the items within the section
                    items(section) { item ->
                        if (item.type == MessageTypes.GROUP_NOTIFICATION) {
                            GroupNotificationView(
                                item = item,
                                userData = userData,
                                userList = userList
                            )
                        } else if (item.senderId == userData.uid) {
                            OutgoingChatView(message = item)
                        } else {
                            IncomingChatView(message = item)
                        }
                    }
                    // Add the section header
                    item {
                        DateHeader(date = date)
                    }
                }
            }
            ChatMessageBox { value ->
                Log.e("message : ", value)
            }
        }
    }
}


@Composable
fun DateHeader(date: String, modifier: Modifier = Modifier) {
    Text(
        text = date,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = modifier.fillMaxWidth(),
    )
}


@Preview(showBackground = true)
@Composable
fun DateHeaderPreview() {
    DateHeader(date = "Today")
}


