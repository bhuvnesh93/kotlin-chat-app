package com.example.chatapp.model

import com.google.firebase.database.PropertyName

data class MessageData(
    @get:PropertyName("sender_id") @set:PropertyName("sender_id")
    var senderId: String = "",
    val timestamp: Long = 0,
    @get:PropertyName("message_id") @set:PropertyName("message_id")
    var messageId: String = "",
    @get:PropertyName("message_type") @set:PropertyName("message_type")
    var messageType: String = "",
    val type: String = "",
    var members: List<Members>,
    var message: String = "",
    var url: String = "",
) {
    constructor() : this("", 0, "", "", "", emptyList(), "","")
}

data class Members(
    val uid: String = "",
) {
    constructor() : this("")
}

