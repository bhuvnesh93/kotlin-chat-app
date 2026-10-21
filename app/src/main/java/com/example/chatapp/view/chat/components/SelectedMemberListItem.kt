package com.example.chatapp.view.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.components.AppImage
import com.example.chatapp.model.UserData


@Composable
fun SelectedMemberListItem(selectedMember: UserData, onDelete: () -> Unit) {
//    LazyRow(
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        modifier = Modifier
//            .height(100.dp) // Give LazyRow a fixed height
//            .background(Color.White)
//            .padding(8.dp)
//    ) {
//        items(selectedMembers) { item ->
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = {
                onDelete()
            }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            AppImage(selectedMember.imageUrl, R.drawable.ic_user_placeholder, 50.dp, true) {
            }
            IconButton(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Favorite",
                    tint = Color.LightGray
                )
            }
        }
        Text(
            selectedMember.name,
            modifier = Modifier.padding(top = 5.dp),
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal),
            maxLines = 1, // Restricts the text to a single line
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedMemberListItemPreview() {
    SelectedMemberListItem(
        selectedMember = UserData(
            uid = "123",
            name = "Bhuvneshwar", imageUrl = "",
            status = "Online", online = true, email = "garg@gmail.com", lastSeenOnline = 4343434
        ), onDelete = {})
}
