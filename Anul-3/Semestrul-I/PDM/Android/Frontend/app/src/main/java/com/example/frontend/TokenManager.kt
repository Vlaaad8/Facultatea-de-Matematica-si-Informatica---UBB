package com.example.frontend

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


val Context.dataStore by preferencesDataStore(name = "user_prefs")

object TokenManager {
    private val KEY_TOKEN = stringPreferencesKey("auth_token")

    var token: String? = null
        private set

    fun loadToken(context: Context) {
        runBlocking {
            token = context.dataStore.data.map { prefs -> prefs[KEY_TOKEN] }.first()
        }
    }

    suspend fun saveToken(context: Context, newToken: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = newToken
        }
        token = newToken
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_TOKEN)
        }
        token = null
    }
}