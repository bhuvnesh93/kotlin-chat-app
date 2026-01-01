package com.example.chatapp.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.ChatEvents
import com.example.chatapp.model.FirebaseRepository
import com.example.chatapp.model.GroupData
import com.example.chatapp.model.MessageData
import com.example.chatapp.model.UserData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()

    private val _userList = MutableStateFlow<List<UserData>>(emptyList())
    val userList: StateFlow<List<UserData>> = _userList

    private val _userChatGroups = MutableStateFlow<Map<String, GroupData>>(emptyMap())
    val userChatGroups: StateFlow<Map<String, GroupData>> = _userChatGroups

    private val _chatMessages =
        MutableStateFlow<Map<String, List<MessageData>>>(emptyMap())
    val chatMessages: StateFlow<Map<String, List<MessageData>>> = _chatMessages.asStateFlow()

    fun saveUsersList(userData: UserData) {
        viewModelScope.launch {
            firebaseRepository.fetchUserList(userData).collect {
                _userList.value = it
            }
        }
    }

    fun fetchMessagesByGroup(
        groupId: String,
        deleteTill: Long,
    ) {
        viewModelScope.launch {
            firebaseRepository.fetchMessagesByGroup(groupId, deleteTill).collect { it ->
                val sortedMessages = it.sortedByDescending { it.timestamp }
                _chatMessages.value = sortedMessages.groupBy { message ->
                    // Format the timestamp into a readable date string
                    // You can customize the format and use utility functions (like for "Today"/"Yesterday")
                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    dateFormat.format(Date(message.timestamp))
                }.toSortedMap(Comparator.reverseOrder())
            }
        }
    }

    fun updateOnlineOfflineStatus(
        uid: String,
        isOnline: Boolean,
    ) {
        viewModelScope.launch {
            firebaseRepository.updateOnlineOfflineStatus(uid, isOnline)
        }
    }

    fun saveUserChatGroups(userData: UserData) {
//        viewModelScope.launch {
//            val initialData = hashMapOf("Item A" to true, "Item B" to false, "Item C" to true)
//            _userChatGroups.value = initialData
//            delay(2000)
//            _userChatGroups.update { currentMap ->
//                (currentMap + ("Item D" to true)) as HashMap<String, Boolean>
//            }
//        }
        viewModelScope.launch {
            firebaseRepository.fetchUserChatGroups(userData).collect {
                when (it) {
                    is ChatEvents.GroupModel -> {
                        _userChatGroups.update { currentMap ->
                            (currentMap + (it.groupModel.groupId to it.groupModel))
                        }
                    }

                    is ChatEvents.GroupValue -> {
                        val updatedMap = _userChatGroups.value.toMutableMap()
                        updatedMap.remove(it.groupId)
                        _userChatGroups.value = updatedMap
                    }
                }
            }
        }
    }

    fun createGroup(
        groupName: String,
        selfUserId: String,
        users: List<UserData>,
        imageUrl: String,
        callback: (groupId: String) -> Unit,
    ) {
        viewModelScope.launch {
            firebaseRepository.createGroup(groupName, selfUserId, users, imageUrl) { it ->
                callback(it)
            }
        }
    }

    fun uploadImage(
        path: Uri,
        storagePath: String,
        callback: (String) -> Unit,
    ) {
        viewModelScope.launch {
            firebaseRepository.uploadImage(path, storagePath) { it ->
                callback(it)
            }
        }
    }

    fun uploadProfilePic(
        path: Uri,
        storagePath: String,
        callback: (String) -> Unit,
    ) {
        viewModelScope.launch {
            uploadImage(path, storagePath) { it ->
                // update download url
                callback(it)
            }
        }
    }
}
