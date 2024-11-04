package com.sujoy.hernavigator.ViewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sujoy.hernavigator.ViewModel.BudgetViewModel

class BudgetViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}