package com.example.chatapp.model

import com.google.firebase.database.PropertyName

data class GroupData(
    @get:PropertyName("group_id") @set:PropertyName("group_id")
    var groupId: String = "",
    val name: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url")
    var imageUrl: String = "",
    @get:PropertyName("created_by") @set:PropertyName("created_by")
    var createdBy: String = "",
    var members: HashMap<String, GroupMembers>,
    var lastMessage: LastMessage?,
    val timestamp: Long = 0,
    val group: Boolean = false,
    @get:PropertyName("group_deleted") @set:PropertyName("group_deleted")
    var groupDeleted: Boolean = false,
) {
    constructor() : this("", "", "", "", HashMap(), LastMessage(), 0, false, false)
}

data class LastMessage(
    var message: String = "",
    @get:PropertyName("message_id") @set:PropertyName("message_id")
    var messageId: String = "",
    @get:PropertyName("message_type") @set:PropertyName("message_type")
    var messageType: String = "",
    @get:PropertyName("sender_id") @set:PropertyName("sender_id")
    var senderId: String = "",
    val timestamp: Long = 0L,
    val type: String = "",
) {
    constructor() : this("", "", "", "", 0L, "")
}

data class GroupMembers(
    @get:PropertyName("unread_group_count") @set:PropertyName("unread_group_count")
    var unreadGroupCount: Int = 0,
    @get:PropertyName("last_seen_message_timestamp") @set:PropertyName("last_seen_message_timestamp")
    var lastSeenMessageTimestamp: Long = 0L,
    val uid: String = "",
    @get:PropertyName("delete_till") @set:PropertyName("delete_till")
    var deleteTill: Long = 0L,
    val active: Boolean = false,
    val admin: Boolean? = false,
) {
    constructor() : this(0, 0L, "", 0, false, false)
}