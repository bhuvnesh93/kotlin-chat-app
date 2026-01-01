package com.example.chatapp.view.chat.chatviews

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.chatapp.constants.NotificationMessageTypes
import com.example.chatapp.model.MessageData
import com.example.chatapp.model.UserData
import com.example.chatapp.viewmodel.ChatViewModel

@Composable
fun GroupNotificationView(
    item: MessageData,
    userData: UserData,
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
) {
    var message by remember { mutableStateOf("") }

    val userList by chatViewModel.userList.collectAsState()

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

    Text(
        text = message,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.fillMaxWidth(),

        // Add styling and padding as needed
    )
}