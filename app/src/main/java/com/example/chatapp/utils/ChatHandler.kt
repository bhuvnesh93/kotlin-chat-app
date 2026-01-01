package com.example.chatapp.utils

import android.util.Log
import com.example.chatapp.model.UserData
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

fun uid(uid: String): String {
    return "uid-$uid"
}

fun generatePrivateChatId(selfUserId: String, otherUserId: String): String {
    val a = selfUserId.replace("uid-", "")
    val b = otherUserId.replace("uid-", "")
    Log.e("aTemp : ", a)
    Log.e("bTemp : ", b)
    var id = ""
    id = if (a > b)
        otherUserId + "_" + selfUserId
    else
        selfUserId + "_" + otherUserId
    return id
}

fun createOrUpdateUser(user: UserData, callback: () -> Unit) {
    val database: DatabaseReference = Firebase.database.reference
    database.child("chat").child("users").child(user.uid).get().addOnSuccessListener {
        if (it.value != null) {
            database.child("chat").child("users").child(user.uid).child("online").setValue(true)
            callback()
        } else {
            database.child("chat").child("users").child(user.uid).setValue(user)
        }

    }.addOnFailureListener {
        Log.e("firebase", "Error getting data", it)
    }
}

