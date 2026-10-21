package com.example.chatapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.AuthRepository
import com.example.chatapp.model.UserData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _userData = MutableStateFlow(UserData("", "", "", "", false, "", 0));
    val userData: StateFlow<UserData> = _userData

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000)
            authRepository.getLoginData(getApplication())
                .collect { it ->
                    _userData.value = it
                    _isLoading.value = false
                }
        }
    }

//    val userPreferences: StateFlow<UserData> =
//        authRepository.getLoginData().stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Companion.WhileSubscribed(5000),
//            initialValue = UserData("", "", false)
//        )

    fun saveUserData(userData: UserData) {
        viewModelScope.launch {
            authRepository.saveLoginData(
                getApplication(),
                userData,
            )
        }
    }

    fun updateProfilePicData(url: String) {
        viewModelScope.launch {
            authRepository.updateProfilePicData(
                getApplication(),
                url,
            )
        }
    }
}