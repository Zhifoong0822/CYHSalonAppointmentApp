package com.example.cyhsalonappointment.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore instance
private val Context.dataStore by preferencesDataStore(name = "user_session")

class UserSessionManager(private val context: Context) {

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")

        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // Save user ID
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
        }
    }

    // Save login status
    suspend fun saveLoginStatus(loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_EMAIL] = email
        }
    }

    fun getUserEmail(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_USER_EMAIL] ?: ""
        }
    }

    // Get user ID
    fun getUserId(): Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }

    // Get login status
    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }

    // Clear all data (logout)
    suspend fun clearSession() {
        context.dataStore.edit { prefs -> prefs.clear() }
    }
}