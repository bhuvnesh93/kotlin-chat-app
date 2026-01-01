package com.example.chatapp.model

sealed class ChatEvents {
    data class GroupModel(val groupModel: GroupData) : ChatEvents()
    data class GroupValue(val groupId: String) : ChatEvents()
}