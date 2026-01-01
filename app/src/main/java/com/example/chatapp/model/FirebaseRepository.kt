package com.example.chatapp.model

import android.net.Uri
import android.util.Log
import com.example.chatapp.constants.MessageTypes
import com.example.chatapp.constants.NotificationMessageTypes
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepository {

    private val db = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    fun createOrUpdateUser(user: UserData, callback: () -> Unit) {
        val database: DatabaseReference = Firebase.database.reference
        database.child("chat").child("users").child(user.uid).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
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

    fun fetchUserChatGroups(userData: UserData): Flow<ChatEvents> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for ((key, value) in snapshot.value as HashMap<*, *>) {
                        if (value == true) {
                            db.child("chat").child("group").child(key.toString()).get()
                                .addOnSuccessListener {
                                    val groupModel = it.getValue(GroupData::class.java) as GroupData
                                    trySend(ChatEvents.GroupModel(groupModel)).isSuccess
                                }

                                .addOnFailureListener {
                                    Log.e("data : ", "Error")
                                }
                        } else {
                            trySend(ChatEvents.GroupValue(key as String)).isSuccess
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        db.child("chat").child("users").child(userData.uid).child("group")
            .addValueEventListener(listener)

        awaitClose { db.removeEventListener(listener) }
    }

    fun fetchUserList(userData: UserData): Flow<List<UserData>> = callbackFlow {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val dataMap =
                        dataSnapshot.children.mapNotNull { it.getValue(UserData::class.java) }
                    val myList = dataMap.filter { it ->
                        it.uid != userData.uid
                    }
                    trySend(myList).isSuccess
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        db.child("chat").child("users").addValueEventListener(valueEventListener)
        awaitClose { db.removeEventListener(valueEventListener) }
    }

    fun fetchMessagesByGroup(groupId: String, deleteTill: Long): Flow<List<MessageData>> =
        callbackFlow {
            db.child("chat")
                .child("messages")
                .child(groupId)
                .orderByChild("timestamp")
                .startAt(deleteTill.toDouble())
                .get().addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val dataMap =
                            dataSnapshot.children.mapNotNull { it.getValue(MessageData::class.java) }
                        println(dataMap.toString())
                        trySend(dataMap)
                    }
                }
            awaitClose { }
        }

    fun updateOnlineOfflineStatus(
        uid: String,
        isOnline: Boolean,
    ) {
        val millis = System.currentTimeMillis()
        val updates = hashMapOf<String, Any>(
            "online" to isOnline,
            "last_seen_online" to millis
        )
        db.child("chat").child("users").child(uid).updateChildren(updates).addOnSuccessListener {
            println("User data updated!")
        }.addOnFailureListener { it ->
            println("Error updating user data: ${it.message}")
        }
    }

    fun sendNotificationMessageToGroup(
        groupId: String, selfUserId: String,
        messageType: String,
        members: List<Members>,
        callback: () -> Unit,
    ) {
        val millis = System.currentTimeMillis()
        val messageId = db.push().key.toString()
        val messageModel = MessageData(
            selfUserId,
            millis,
            messageId,
            messageType,
            MessageTypes.GROUP_NOTIFICATION,
            members
        )

        db.child("chat").child("messages")
            .child(groupId).child(messageId).setValue(messageModel)
            .addOnSuccessListener { it ->
                println("added success")
                callback()
            }
            .addOnFailureListener { it ->
                println("added failed")

            }
    }

    fun createGroup(
        groupName: String,
        selfUserId: String,
        users: List<UserData>,
        imageUrl: String,
        callback: (groupId: String) -> Unit,
    ) {
        val millis = System.currentTimeMillis()
        val adminUserModel = GroupMembers(
            0,
            millis,
            selfUserId,
            millis,
            active = true,
            admin = true
        )
        val groupMembers = HashMap<String, GroupMembers>()
        val membersArr = mutableListOf<Members>();
        groupMembers[adminUserModel.uid] = adminUserModel
        users.forEach { item ->
            membersArr.add(Members(item.uid))
            val otherUserModel = GroupMembers(
                0, millis, item.uid, millis,
                active = true,
                admin = false
            )
            groupMembers[otherUserModel.uid] = otherUserModel
        }

        val groupId = db.push().key.toString()
        val newGroup = GroupData(
            groupId,
            groupName,
            imageUrl,
            adminUserModel.uid,
            groupMembers,
            null,
            millis,
            true
        )
        db.child("chat").child("group").child(groupId).setValue(newGroup)
            .addOnSuccessListener { it ->
                println("Group Created")
                for (keys in newGroup.members) {
                    db.child("chat").child("users").child(keys.key).child("group").child(groupId)
                        .setValue(true)
                        .addOnSuccessListener {
                            println("update user group success")
                        }
                        .addOnFailureListener { it -> println("update user group failed") }
                }
                sendNotificationMessageToGroup(
                    groupId,
                    selfUserId,
                    NotificationMessageTypes.NEW_GROUP,
                    emptyList(),
                ) {
                    sendNotificationMessageToGroup(
                        groupId,
                        selfUserId,
                        NotificationMessageTypes.ADD_MEMBER,
                        membersArr,
                    ) {
                        callback(groupId)
                    }
                }
            }.addOnFailureListener { it ->
                println("Group Creation failed")
            }
    }

    fun uploadImage(
        path: Uri,
        storagePath: String, callback: (String) -> Unit,
    ) {
        val millis = System.currentTimeMillis()

        val imageRef = storageRef.child("$storagePath/${millis}_gallery")
        imageRef.putFile(path).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                callback(imageUrl)
            }
        }
            .addOnFailureListener { e ->
                println("Upload failed: ${e.message}")
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                // Update UI with progress if desired
                println("Upload progress: $progress%")
            }
    }

    fun updateFCMTokenAndDeviceId() {
        //
    }

    fun removeFCMTokenAndDeviceIdOnSignOut() {
        //
    }

    // new chat screen
    fun createOneOnOneChatGroup() {
        //
    }

    // chat message
    fun sendMessageToChat() {
        //
    }

    fun fetchMessagesByGroupPagination() {
        //
    }

    fun updateGroupName() {
        //
    }

    fun removeImage() {
        //
    }

    fun updateGroupImage() {
        //
    }

    fun addMemberToGroup() {
        //
    }

    fun changeAdminStatusOfUser() {
        //
    }

    fun clearChatHistory() {
        //
    }

    fun removeMemberFromGroup() {
        //
    }

    fun exitGroup() {
        //
    }

    fun deleteGroupForSelfAfterExit() {
        //
    }

    fun deleteMessage() {
        //
    }

    fun editMessage() {
        //
    }

    fun uploadMediaImage() {
        //
    }

    fun updateUnReadCountLastSeenMessageTimestamp() {
        //
    }

    fun chatIsToday() {
        //
    }

    fun chatIsYesterday() {
        //
    }

    fun chatFormatDateDDMonthYYYY() {
//
    }

    fun chatFormatTimeAMPM() {
        //
    }

//    fun addItem(item: Item) {
//        val newItemRef = database.push()
//        val newItem = item.copy(id = newItemRef.key ?: "")
//        newItemRef.setValue(newItem)
//    }
}