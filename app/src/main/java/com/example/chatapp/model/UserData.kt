package com.example.chatapp.model

import com.google.firebase.database.PropertyName

data class UserData(
    val uid: String = "",
    val name: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url")
    var imageUrl: String = "",
    val status: String = "",
    val online: Boolean = false,
    val email: String = "",
    @get:PropertyName("last_seen_online") @set:PropertyName("last_seen_online")
    var lastSeenOnline: Long = 0L
) {
    constructor() : this("", "", "", "", false, "", 0)
}
