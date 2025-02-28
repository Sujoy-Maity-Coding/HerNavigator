package com.sujoy.hernavigator.Api.api.auth_api

import com.sujoy.hernavigator.Api.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES).build()

    val api: API_Builder = Retrofit.Builder().client(client)
        .baseUrl(Constant.authBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(API_Builder::class.java)
}