package com.sujoy.hernavigator.Api.data.Pref

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { prefs -> prefs[USER_ID_KEY] = userId }
    }

    fun getUserId(): String? {
        return runBlocking { dataStore.data.first()[USER_ID_KEY] }
    }
}
