package com.example.chatapp.view.chat.chatviews

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.model.Members
import com.example.chatapp.model.MessageData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OutgoingChatView(message: MessageData) {
    val simpleDateFormat =
        SimpleDateFormat("HH:mm", Locale.getDefault()) // Define format pattern
    val formattedTime = simpleDateFormat.format(message.timestamp)

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    // Calculate 70% of the screen width in Dp
    val seventyPercentWidth = screenWidthDp * 0.7f

    Row(
        modifier = Modifier.fillMaxWidth(), // Or a specific width
        horizontalArrangement = Arrangement.End // Align the content (which is our Column) to the right
    ) {
        Column(
            Modifier
                .widthIn(min = 100.dp)
                .widthIn(max = seventyPercentWidth)
                .padding(8.dp)
                .background(
                    colorResource(id = R.color.chat_outer_cell),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
        ) {
            if (message.messageType == "TEXT") {
                Text(
                    text = message.message,
                    style = TextStyle(color = Color.Black, fontSize = 14.sp)
                )
            } else {
                AsyncImage(
                    model = message.url,
                    contentDescription = "",
                    contentScale = ContentScale.Fit, // Crop to fill the circular shape
                    modifier = Modifier.height(300.dp)
                )
            }
            Text(
                text = formattedTime,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp),
                style = TextStyle(color = Color.Black, fontSize = 11.sp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutgoingChatViewPreview() {
    OutgoingChatView(
        message = MessageData(
            senderId = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2",
            timestamp = 1761193118681,
            messageId = "-OcEHZjbOZKzuO0dS1Fw",
            messageType = "TEXT",
            type = "GROUP_NOTIFICATION",
            members = listOf(Members(uid = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2")),
            message = "Hi Bhuvneshwar garg dsdsdsdsdsds dfdfdsfasdfasdfadf dssadad"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun OutgoingChatViewLess() {
    OutgoingChatView(
        message = MessageData(
            senderId = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2",
            timestamp = 1761193118681,
            messageId = "-OcEHZjbOZKzuO0dS1Fw",
            messageType = "TEXT",
            type = "GROUP_NOTIFICATION",
            members = listOf(Members(uid = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2")),
            message = "Hi"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun OutgoingChatViewImage() {
    OutgoingChatView(
        message = MessageData(
            senderId = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2",
            timestamp = 1761193118681,
            messageId = "-OcEHZjbOZKzuO0dS1Fw",
            messageType = "IMAGE",
            type = "GROUP_NOTIFICATION",
            members = listOf(Members(uid = "uid - Lyc0rdYz4sSRQLBdkGivTyBwPKB2")),
            message = "Hi"
        )
    )
}