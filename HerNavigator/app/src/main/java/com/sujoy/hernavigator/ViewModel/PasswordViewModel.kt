package com.sujoy.hernavigator.ViewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.sujoy.hernavigator.Model.PasswordEntry
import com.sujoy.hernavigator.Presentation.Screen.PasswordStorage.getEncryptedPrefs

class PasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs: SharedPreferences = getEncryptedPrefs(application)
    var passwords = mutableStateListOf<PasswordEntry>()
        private set

    init {
        loadPasswords()
    }

    private fun loadPasswords() {
        passwords.clear()
        prefs.all.forEach { entry ->
            if (entry.key != "app_password") {
                passwords.add(PasswordEntry(entry.key, entry.value as String))
            }
        }
    }

    // Save app password on first launch
    fun setAppPassword(password: String) {
        if (!isPasswordSet()) {
            prefs.edit().putString("app_password", password).apply()
        }
    }

    // Check if password is set
    fun isPasswordSet(): Boolean {
        return prefs.contains("app_password")
    }

    // Validate entered password
    fun validatePassword(password: String): Boolean {
        val savedPassword = prefs.getString("app_password", null)
        return savedPassword == password
    }

    fun addPassword(service: String, password: String) {
        prefs.edit().putString(service, password).apply()
        passwords.add(PasswordEntry(service, password))
    }

    fun deletePassword(service: String) {
        prefs.edit().remove(service).apply()
        passwords.removeIf { it.service == service }
    }
}