package com.sujoy.hernavigator.Presentation.Screen.OnBoardinScreen

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("onboarding_prefs")

class OnboardingPreferences(private val context: Context) {
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_complete")

    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[ONBOARDING_KEY] ?: false }

    suspend fun setOnboardingComplete() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = true
        }
    }
}