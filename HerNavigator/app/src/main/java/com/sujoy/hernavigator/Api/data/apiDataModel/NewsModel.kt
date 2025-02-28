package com.sujoy.hernavigator.Api.data.apiDataModel

data class NewsModel(
    val articles: List<Article?>?,
    val status: String?,
    val totalResults: Int?
)