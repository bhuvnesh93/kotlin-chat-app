package com.example.chatapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapp.model.UserData
import com.example.chatapp.view.authentication.LoginScreen
import com.example.chatapp.view.authentication.RegisterScreen
import com.example.chatapp.view.chat.ChatMessageScreen
import com.example.chatapp.view.chat.ChatsScreen
import com.example.chatapp.view.chat.NewChatScreen
import com.example.chatapp.view.chat.group.AddMemberScreen
import com.example.chatapp.view.chat.group.AddNewMemberScreen
import com.example.chatapp.view.chat.group.CreateGroupScreen
import com.example.chatapp.view.chat.group.GroupDetailScreen
import com.example.chatapp.view.profile.OtherUserProfileScreen
import com.example.chatapp.view.profile.ProfileDetailScreen
import com.example.chatapp.viewmodel.ChatViewModel
import com.example.chatapp.viewmodel.UserViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import java.util.Map.entry

@Composable
fun Navigation(
    viewModel: UserViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    chatViewModel: ChatViewModel = viewModel(),
) {
    val userData by viewModel.userData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    if (!isLoading) {
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            CircularProgressIndicator()
//        }
//    } else {
        NavHost(
            navController = navController,
            startDestination = if (userData.email.isNotEmpty()) Screen.ChatsScreen.route else Screen.LoginScreen.route
        ) {
            composable(Screen.LoginScreen.route) {
                LoginScreen(navController, viewModel)
            }

            composable(Screen.RegisterScreen.route) {
                RegisterScreen(navController, viewModel)
            }

            composable(Screen.ChatsScreen.route) {
                ChatsScreen(navController, viewModel, chatViewModel)
            }

            composable(Screen.ChatMessagesScreen.route + "/{groupId}/{chatType}/{fromRoute}") { entry ->
                val groupId = entry.arguments!!.getString("groupId") ?: ""
                val chatType = entry.arguments!!.getString("chatType") ?: ""
                val fromRoute = entry.arguments!!.getString("fromRoute") ?: ""
                ChatMessageScreen(
                    navController,
                    viewModel,
                    chatViewModel,
                    groupId,
                    chatType,
                    fromRoute
                )
            }

            composable(Screen.AddMemberScreen.route) {
                AddMemberScreen(navController, viewModel, chatViewModel)
            }

            composable(
                Screen.AddNewMemberScreen.route + "/{addedMembers}/{groupId}",
            ) { entry ->
                val addedMembersJson = entry.arguments!!.getString("addedMembers")
                Log.e("sdsdsdsdsdsds", addedMembersJson.toString())
                val addedMembers: List<UserData> =
                    addedMembersJson.let {
                        Gson().fromJson(
                            it,
                            object : TypeToken<List<UserData>>() {}.type
                        )
                    }

                val groupId = entry.arguments!!.getString("groupId") ?: ""
                AddNewMemberScreen(navController, viewModel, chatViewModel, addedMembers, groupId)
            }

            composable(Screen.CreateGroupScreen.route + "/{selectedMembers}") { entry ->
                val usersJson = entry.arguments?.getString("selectedMembers")
                val users: List<UserData> =
                    usersJson.let {
                        Gson().fromJson(
                            it,
                            object : TypeToken<List<UserData>>() {}.type
                        )
                    }
                CreateGroupScreen(navController, viewModel, chatViewModel,users)
            }

            composable(Screen.GroupDetailScreen.route + "/{groupId}") { entry ->
                val groupId = entry.arguments!!.getString("groupId") ?: ""
                GroupDetailScreen(navController, viewModel, chatViewModel, groupId)
            }

            composable(Screen.NewChatScreen.route) {
                NewChatScreen(navController, viewModel, chatViewModel)
            }

            composable(Screen.ProfileDetailScreen.route) {
                ProfileDetailScreen(navController, viewModel, chatViewModel)
            }

            composable(Screen.OtherUserProfileScreen.route + "/{otherUserId}") { entry ->
                val otherUserId = entry.arguments!!.getString("otherUserId") ?: ""
                OtherUserProfileScreen(navController, chatViewModel, otherUserId)
            }
        }
    }

}
