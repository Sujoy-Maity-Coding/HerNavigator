package com.sujoy.hernavigator.ViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sujoy.hernavigator.ViewModel.SafetyViewModel

class SafetyViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SafetyViewModel::class.java)) {
            return SafetyViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}