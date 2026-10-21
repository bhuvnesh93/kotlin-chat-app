package com.example.chatapp.view.profile

import android.R.attr.onClick
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.components.AppBarView
import com.example.chatapp.components.AppImage
import com.example.chatapp.ui.theme.Pink80
import com.example.chatapp.ui.theme.Purple80
import com.example.chatapp.ui.theme.Typography
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    navController: NavController,
    viewModel: UserViewModel,
    chatViewModel: ChatViewModel,
) {
    val userData by viewModel.userData.collectAsState()

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                chatViewModel.uploadProfilePic(it, "user") { imageUrl ->
                    viewModel.updateProfilePicData(imageUrl)
                }
            }
        }
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            AppBarView(
                "Profile", onBackNavClicked = { navController.popBackStack() }
            )
        }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(15.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppImage(userData.imageUrl, R.drawable.ic_user_placeholder, 100.dp, true) {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
//                AppImage(
//                    userData.imageUrl, R.drawable.ic_user_placeholder, Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .clickable(onClick = {
//                            pickMediaLauncher.launch(
//                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                            )
//                        })
//                )
                Text("Edit", modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            }
            Column {
                Text(
                    text = "Name",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        fontFamily = FontFamily.Serif
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .padding(5.dp)
                        .clickable(onClick = { navController.navigate("edit_profile") })
                ) {
                    Text(
                        text = userData.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "Edit",
                    )
                }
                Text("Email", style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium))
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .background(Color.White, shape = RoundedCornerShape(5.dp))
                        .padding(5.dp)
                        .clickable(onClick = { navController.navigate("edit_profile") })
                ) {
                    Text(
                        text = userData.email,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "Edit",
                    )
                }
            }
        }
    }
}