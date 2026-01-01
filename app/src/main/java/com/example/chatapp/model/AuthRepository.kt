package com.example.chatapp.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferencesKeys {
    val UID = stringPreferencesKey("uid")
    val NAME = stringPreferencesKey("name")
    val EMAIL = stringPreferencesKey("email")
    val IMAGEURL = stringPreferencesKey("imageUrl")
}

class AuthRepository() {
    suspend fun saveLoginData(
        context: Context,
        userData: UserData,
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.UID] = userData.uid
            preferences[PreferencesKeys.NAME] = userData.name
            preferences[PreferencesKeys.EMAIL] = userData.email
        }
    }

    suspend fun updateProfilePicData(context: Context, url: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IMAGEURL] = url
        }
    }

    fun getLoginData(context: Context): Flow<UserData> {
        return context.dataStore.data.map { preferences ->
            UserData(
                uid = preferences[PreferencesKeys.UID].toString(),
                name = preferences[PreferencesKeys.NAME] ?: "",
                email = preferences[PreferencesKeys.EMAIL] ?: "",
                imageUrl = "",
                status = "",
                online = false,
                lastSeenOnline = 0
            )
        }
    }
}

