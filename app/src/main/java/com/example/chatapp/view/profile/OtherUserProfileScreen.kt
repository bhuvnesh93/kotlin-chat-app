package com.example.chatapp.view.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel

@Composable
fun OtherUserProfileScreen(
    navController: NavController,
    chatViewModel: ChatViewModel,
    otherUserId: String,
) {
    val userList by chatViewModel.userList.collectAsStateWithLifecycle()

    val otherUserDetail = userList.find { it ->
        it.uid == otherUserId
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "Profile", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AsyncImage(
                placeholder = painterResource(R.drawable.ic_user_placeholder),
                error = painterResource(id = R.drawable.ic_user_placeholder),
                model = otherUserDetail!!.imageUrl,
                contentDescription = "",
                contentScale = ContentScale.Crop, // Crop to fill the circular shape
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape) // Apply circular clip
            )
            Text(
                text = otherUserDetail.name, modifier = Modifier.padding()
            )
        }
    }
}