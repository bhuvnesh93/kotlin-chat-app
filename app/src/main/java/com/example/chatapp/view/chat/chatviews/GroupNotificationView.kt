package com.example.chatapp.view.chat.chatviews

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.constants.NotificationMessageTypes
import com.example.chatapp.model.Members
import com.example.chatapp.model.MessageData
import com.example.chatapp.model.UserData
import com.example.chatapp.viewmodel.ChatViewModel

@Composable
fun GroupNotificationView(
    item: MessageData,
    userData: UserData,
    userList: List<UserData>,
) {
    var message by remember { mutableStateOf("") }
//    val userList by chatViewModel.userList.collectAsState()

    Log.e("***** item : ", item.toString())
    Log.e("***** userData : ", userData.toString())

    LaunchedEffect(item) {
        var name = userList.find { it.uid == item.senderId }?.name
        if (item.senderId == userData.uid) {
            name = "You"
        }

        when (item.messageType) {
            NotificationMessageTypes.NEW_GROUP -> {
                message = "$name created this group"
                Log.e("message : ", message)
            }

            NotificationMessageTypes.ADD_MEMBER -> {
                var membersName = ""
                membersName = if (item.members[0].uid === userData.uid) {
                    "You"
                } else {
                    val name = userList.find { it.uid == item.members[0].uid }?.name
                    if (item.members.size > 1) {
                        if (item.members.size - 1 > 1) {
                            "$name and ${item.members.size - 1} others"
                        } else {
                            "$name and ${item.members.size - 1} other"
                        }
                    } else {
                        "$name"
                    }
                }
                message = "$name added $membersName"
            }

            NotificationMessageTypes.REMOVE_MEMBER -> {
                var membersName = ""
                membersName = if (item.members[0].uid === userData.uid) {
                    "You"
                } else {
                    val name = userList.find { it.uid == item.members[0].uid }?.name
                    "$name"
                }
                message = "$name removed $membersName"
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 13.sp, color = Color.Black),
            modifier = Modifier
                .padding(3.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun GroupNotificationViewPreview() {
    GroupNotificationView(
        item = MessageData(
            senderId = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2",
            timestamp = 1761193118681,
            messageId = "-OcEHZjbOZKzuO0dS1Fw",
            messageType = "NEW_GROUP",
            type = "GROUP_NOTIFICATION",
            members = listOf(Members(uid = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2")),
            message = ""
        ),
        userData = UserData(
            uid = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2",
            name = "Shami",
            imageUrl = "",
            status = "",
            online = false,
            email = "shami@gmail.com",
            lastSeenOnline = 0
        ),
        userList = listOf(
            UserData(
                uid = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2",
                name = "Shami",
                imageUrl = "",
                status = "",
                online = false,
                email = "shami@gmail.com",
                lastSeenOnline = 0
            )
        )
    )
}