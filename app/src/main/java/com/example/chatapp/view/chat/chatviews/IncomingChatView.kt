package com.example.chatapp.view.chat.chatviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.model.MessageData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun IncomingChatView(message: MessageData, modifier: Modifier = Modifier) {

    val simpleDateFormat =
        SimpleDateFormat("HH:mm", Locale.getDefault()) // Define format pattern
    val formattedTime = simpleDateFormat.format(message.timestamp)

    Column(
        modifier
            .wrapContentWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .background(colorResource(id = R.color.chat_incoming_cell)),
    ) {
        Text(
            text = message.message,
            modifier = modifier.padding(horizontal = 10.dp)

            // Add styling and alignment (e.g., sender-specific background)
        )
        Text(
            text = formattedTime,
            modifier = modifier.padding(horizontal = 10.dp),
            style = MaterialTheme.typography.titleSmall,
            // Add styling and alignment (e.g., sender-specific background)
        )
    }


}