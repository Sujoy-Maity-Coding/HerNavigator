package com.sujoy.hernavigator.Api.api.api_image_noti

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Retrofit Instance with Timeout and Logging
object RetrofitClient {
    const val BASE_URL_1 = "https://sujoydev.pythonanywhere.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // Logs request & response
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // 60s connection timeout
        .readTimeout(60, TimeUnit.SECONDS)  // 60s read timeout
        .writeTimeout(60, TimeUnit.SECONDS)  // 60s write timeout
        .addInterceptor(logging)
        .build()

    val apiService: ApiService_1 by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_1)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService_1::class.java)
    }
}