package com.example.chatapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.R

@Composable
fun AppImage(
    imageUrl: String,
    placeholderImage: Int,
    size: Dp,
    clickable: Boolean,
    onClick: () -> Unit,
) {
    AsyncImage(
        placeholder = painterResource(placeholderImage),
        error = painterResource(placeholderImage),
        model = imageUrl,
        contentDescription = "",
        contentScale = ContentScale.Crop, // Crop to fill the circular shape
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(enabled = clickable, onClick = {
                onClick()
            })

    )

//    AsyncImage(
//        placeholder = if (chatItem.group) painterResource(
//            R.drawable.ic_user_placeholder
//        ) else painterResource(R.drawable.ic_user_placeholder),
//        error = if (chatItem.group) painterResource(
//            R.drawable.ic_user_placeholder
//        ) else painterResource(R.drawable.ic_user_placeholder),
//        model = chatItem.imageUrl,
//        contentDescription = "",
//        contentScale = ContentScale.Crop, // Crop to fill the circular shape
//        modifier = Modifier
//            .size(50.dp)
//            .clip(CircleShape) // Apply circular clip
//    )
}

@Preview(showBackground = true)
@Composable
fun AppImagePreview() {
    AppImage(
        "",
        R.drawable.ic_user_placeholder,
        50.dp,
        true
    ) {

    }


}
