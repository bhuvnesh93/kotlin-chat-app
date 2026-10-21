package com.example.chatapp.view.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R

@Composable
fun ChatMessageBox(onSendMessage: (value: String) -> Unit) {
//    var message by remember { mutableStateOf("") }
    val message = rememberTextFieldState("")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.teal_700))
            .padding(10.dp)
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically, // 3. Vertically center the items
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            state = message,
            cursorBrush = SolidColor(Color.White),
            decorator = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    // Manually handle placeholder visibility
                    if (message.text.isEmpty()) {
                        Text("Enter search term...", color = Color.White)
                    }
                    innerTextField() // Mandatory: where the user types
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .border(1.dp, Color.White, RoundedCornerShape(8.dp)),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.White)
        )
        Button(
            onClick = {
                onSendMessage(message.text.toString())
            }, colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Set background color here
                contentColor = Color.Black  // Optional: Set text/icon color
            )
        ) {
            Text("Send", style = TextStyle(fontSize = 15.sp))
        }
    }
}

@Preview
@Composable
fun ChatMessageBoxPreview() {
    ChatMessageBox(onSendMessage = {})
}