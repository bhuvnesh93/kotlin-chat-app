package com.example.chatapp.view.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.components.AppImage
import com.example.chatapp.model.UserData

@Composable
fun NewChatListItem(item: UserData, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppImage(item.imageUrl, R.drawable.ic_user_placeholder, 50.dp, false) {
        }
        Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
            Text(
                text = item.name, style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                text = item.email, modifier = Modifier.padding(top = 3.dp),
                style = TextStyle(fontSize = 13.sp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NewChatListItemPreview() {
    NewChatListItem(
        item = UserData(
            uid = "123",
            name = "Shami", imageUrl = "",
            status = "Online", online = true, email = "garg@gmail.com", lastSeenOnline = 4343434
        )
    ) {}
}