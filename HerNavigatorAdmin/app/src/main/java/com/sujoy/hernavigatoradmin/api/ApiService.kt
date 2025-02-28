package com.sujoy.hernavigatoradmin.api

import com.sujoy.hernavigatoradmin.model.FashionImageResponse
import com.sujoy.hernavigatoradmin.model.Notification
import com.sujoy.hernavigatoradmin.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

// Retrofit API Interface
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("notifications")
    fun addNotification(@Body notification: Map<String, String>): Call<Map<String, String>>

    @GET("notifications")
    fun getNotifications(): Call<List<Notification>>

    @Multipart
    @POST("fashion")
    fun uploadFashionImage(@Part photo: MultipartBody.Part): Call<UploadResponse>

    @GET("fashion/{id}")
    fun getFashionImage(@Path("id") id: Int): Call<ResponseBody>

    @GET("fashion/recent")
    fun getRecentFashionImages(): Call<List<FashionImageResponse>>

}
