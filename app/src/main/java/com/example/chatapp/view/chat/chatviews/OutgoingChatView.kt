package com.example.chatapp.view.chat.chatviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.model.MessageData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OutgoingChatView(message: MessageData, modifier: Modifier = Modifier) {
    val simpleDateFormat =
        SimpleDateFormat("HH:mm", Locale.getDefault()) // Define format pattern
    val formattedTime = simpleDateFormat.format(message.timestamp)

    Row(
        modifier = Modifier.fillMaxWidth(), // Or a specific width
        horizontalArrangement = Arrangement.End // Align the content (which is our Column) to the right
    ) {
        Column(
            modifier
                .wrapContentWidth()
                .padding(8.dp)
                .background(colorResource(id = R.color.chat_outer_cell)),
//            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = message.message,
                modifier = modifier.padding(8.dp).background(Color.Red)

                // Add styling and alignment (e.g., sender-specific background)
            )
            Text(
                text = formattedTime,
                modifier = modifier.padding(8.dp),
                style = MaterialTheme.typography.titleSmall,
                // Add styling and alignment (e.g., sender-specific background)
            )
        }
    }

}