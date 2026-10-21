package com.example.chatapp.view.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.components.AppImage
import com.example.chatapp.model.GroupData
import com.example.chatapp.model.LastMessage
import java.util.HashMap


@Composable
fun ChatsListItem(chatItem: GroupData, onClick: () -> Unit) {
    Column(Modifier.clickable {
        onClick()
    }) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val placeholder: Int = R.drawable.ic_user_placeholder
            if (chatItem.group) {
                R.drawable.ic_group_placeholder
            }
            AppImage(chatItem.imageUrl, placeholder, 50.dp, false) {
//
            }
            Column(
                Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = chatItem.name,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                )
                chatItem.lastMessage?.let {
                    Text(
                        text = it.message,
                        modifier = Modifier.padding(top = 3.dp),
                        style = TextStyle(fontSize = 13.sp)
                    )
                }
            }
        }
        HorizontalDivider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(start = 76.dp)
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun ChatsListItemPreview() {
    ChatsListItem(
        chatItem = GroupData(
            groupId = "123",
            imageUrl = "",
            name = "Bhuvneshwar Garg",
            createdBy = "",
            timestamp = 2323232,
            group = true,
            groupDeleted = false,
            members = HashMap(),
            lastMessage = LastMessage(
                message = "Hello",
                messageId = "123",
            )
        ), onClick = {
//
        })
}
