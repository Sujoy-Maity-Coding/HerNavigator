package com.sujoy.hernavigator.Api.api.api_image_noti

import com.sujoy.hernavigator.Api.data.DataModel.FashionImageResponse
import com.sujoy.hernavigator.Api.data.DataModel.Notification
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService_1 {
    @GET("notifications")
    fun getNotifications(): List<Notification>

    @GET("fashion/{id}")
    fun getFashionImage(@Path("id") id: Int): Call<ResponseBody>

    @GET("fashion/recent")
    fun getRecentFashionImages(): Call<List<FashionImageResponse>>

    @GET("fashion/all")
    fun getAllFashionImages(): Call<List<FashionImageResponse>>

}