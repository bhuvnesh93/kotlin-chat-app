package com.example.chatapp.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.R
import java.net.URL

@Composable
fun AppImage(imageUrl: String, placeholderImage: Int,modifier: Modifier) {

    AsyncImage(
        placeholder = painterResource(placeholderImage),
        error = painterResource(placeholderImage),
        model = imageUrl,
        contentDescription = "",
        contentScale = ContentScale.Crop, // Crop to fill the circular shape
        modifier = modifier
    )
}