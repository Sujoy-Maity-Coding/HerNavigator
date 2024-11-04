package com.sujoy.hernavigator.ViewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class BudgetViewModel(context: Context) : ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("BudgetPrefs", Context.MODE_PRIVATE)

    private val _dailyBudget = MutableStateFlow(sharedPreferences.getString("daily_budget", "") ?: "")
    val dailyBudget: StateFlow<String> get() = _dailyBudget

    private val _totalSpending = MutableStateFlow(sharedPreferences.getFloat("total_spending", 0f).toDouble())
    val totalSpending: StateFlow<Double> get() = _totalSpending

    private val savedDate = sharedPreferences.getString("budget_saved_date", getCurrentDate())

    init {
        // Check if the saved date is today. If not, clear the budget data.
        if (savedDate != getCurrentDate()) {
            clearBudgetData()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun clearBudgetData() {
        viewModelScope.launch {
            _dailyBudget.value = ""
            _totalSpending.value = 0.0
            sharedPreferences.edit()
                .putString("daily_budget", "")
                .putFloat("total_spending", 0f)
                .putString("budget_saved_date", getCurrentDate())
                .apply()
        }
    }

    fun setDailyBudget(dailyBudget: String) {
        viewModelScope.launch {
            _dailyBudget.value = dailyBudget
            sharedPreferences.edit()
                .putString("daily_budget", dailyBudget)
                .putString("budget_saved_date", getCurrentDate())
                .apply()
            // Reset total spending when a new daily budget is set
            _totalSpending.value = 0.0
            sharedPreferences.edit().putFloat("total_spending", _totalSpending.value.toFloat()).apply()
        }
    }

    fun addMoneySpending(amount: Double) {
        viewModelScope.launch {
            _totalSpending.value += amount
            sharedPreferences.edit().putFloat("total_spending", _totalSpending.value.toFloat()).apply()
        }
    }
}

