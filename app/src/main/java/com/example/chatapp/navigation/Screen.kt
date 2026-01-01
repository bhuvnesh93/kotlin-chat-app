package com.example.chatapp.navigation

sealed class Screen(val route:String) {
    data object LoginScreen : Screen("login_Screen")
    data object RegisterScreen : Screen("register_Screen")
    data object ChatsScreen : Screen("chats_Screen")
    data object ChatMessagesScreen : Screen("chat_message_Screen")
    data object NewChatScreen : Screen("new_chat_Screen")
    data object AddMemberScreen : Screen("add_member_Screen")
    data object AddNewMemberScreen : Screen("add_new_member_Screen")
    data object CreateGroupScreen : Screen("create_group_Screen")
    data object GroupDetailScreen : Screen("group_detail_Screen")

    data object ProfileDetailScreen : Screen("profile_detail_Screen")
    data object OtherUserProfileScreen : Screen("other_user_profile_screen")
}