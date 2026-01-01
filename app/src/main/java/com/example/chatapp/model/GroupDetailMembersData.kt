package com.example.chatapp.model

import com.google.firebase.database.PropertyName

data class GroupDetailMembersData(
    val uid: String = "",
    val name: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url")
    var imageUrl: String = "",
    val admin: Boolean?,
) {
    constructor() : this("", "", "", false)
}