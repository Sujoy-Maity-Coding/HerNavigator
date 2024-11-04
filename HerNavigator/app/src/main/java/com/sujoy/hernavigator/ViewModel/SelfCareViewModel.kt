package com.sujoy.hernavigator.ViewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SelfCareViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("self_care_prefs", Context.MODE_PRIVATE)

    // MutableLiveData for holding task data
    private val _tasks = MutableLiveData<Map<String, Pair<String, Int>>>()
    val tasks: LiveData<Map<String, Pair<String, Int>>> = _tasks

    // Initialize by loading saved tasks
    init {
        loadSavedTasks()
    }

    // Function to save tasks to SharedPreferences
    fun saveTask(task: String, time: String, duration: Int) {
        val editor = sharedPreferences.edit()
        editor.putString("${task}_time", time)
        editor.putInt("${task}_duration", duration)
        editor.apply()

        loadSavedTasks() // Refresh the LiveData after saving
    }

    // Function to load saved tasks from SharedPreferences
    private fun loadSavedTasks() {
        val taskMap = mutableMapOf<String, Pair<String, Int>>()

        val tasksList = listOf("Book Reading", "Meditation", "Skincare") // Example tasks
        tasksList.forEach { task ->
            val time = sharedPreferences.getString("${task}_time", "12:00 AM") ?: "12:00 AM"
            val duration = sharedPreferences.getInt("${task}_duration", 0)
            taskMap[task] = Pair(time, duration)
        }

        _tasks.value = taskMap
    }

    // Function to clear task data if needed
    fun clearTasks() {
        sharedPreferences.edit().clear().apply()
        loadSavedTasks()
    }
}
