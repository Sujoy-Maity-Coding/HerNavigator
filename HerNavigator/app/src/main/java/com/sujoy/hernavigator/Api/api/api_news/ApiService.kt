package com.sujoy.hernavigator.Api.api.api_news

import com.sujoy.hernavigator.Api.data.apiDataModel.NewsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //https://newsapi.org/v2/everything?q=women&apiKey=your_api_key

    @GET("v2/everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): NewsModel
}