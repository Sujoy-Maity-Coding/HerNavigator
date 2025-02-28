package com.sujoy.hernavigatoradmin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://sujoydev.pythonanywhere.com/"

// Retrofit Instance with Timeout and Logging
object RetrofitClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // Logs request & response
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // 60s connection timeout
        .readTimeout(60, TimeUnit.SECONDS)  // 60s read timeout
        .writeTimeout(60, TimeUnit.SECONDS)  // 60s write timeout
        .addInterceptor(logging)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
