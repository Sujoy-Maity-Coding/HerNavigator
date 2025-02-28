package com.sujoy.hernavigator.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sujoy.hernavigator.Api.api.api_news.ApiProvider
import com.sujoy.hernavigator.Api.data.apiDataModel.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _news = MutableStateFlow<List<Article>>(emptyList())
    val news: StateFlow<List<Article>> get() = _news

    fun fetchNews(query: String,sortBy:String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = ApiProvider.newsApiService.getNews(query, sortBy, apiKey)
                _news.value = response.articles as List<Article>
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }
}