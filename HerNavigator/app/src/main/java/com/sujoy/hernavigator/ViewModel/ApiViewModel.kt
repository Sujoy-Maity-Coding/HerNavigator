package com.sujoy.hernavigator.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sujoy.hernavigator.Api.api.api_image_noti.RetrofitClient
import com.sujoy.hernavigator.Api.data.DataModel.FashionImageResponse
import com.sujoy.hernavigator.Api.data.DataModel.Notification
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiViewModel : ViewModel() {
    private val apiService = RetrofitClient.apiService

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    private val _uploadStatus = MutableLiveData<String>()
    val uploadStatus: LiveData<String> = _uploadStatus


    private val _imageBitmaps = MutableLiveData<Map<Int, Bitmap?>>()
    val imageBitmaps: LiveData<Map<Int, Bitmap?>> get() = _imageBitmaps

    fun getFashionImage(imageId: Int) {
        apiService.getFashionImage(imageId).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())

                    // Store each image separately
                    _imageBitmaps.postValue(_imageBitmaps.value?.toMutableMap()?.apply {
                        this[imageId] = bitmap
                    } ?: mapOf(imageId to bitmap))
                } else {
                    Log.e("API", "Failed to fetch image $imageId")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API", "Error fetching image: ${t.message}")
            }
        })
    }


    private val _imageIds = MutableLiveData<List<Int>>()
    val imageIds: LiveData<List<Int>> get() = _imageIds

    fun getRecentFashionImages() {
        apiService.getRecentFashionImages().enqueue(object : Callback<List<FashionImageResponse>> {
            override fun onResponse(call: Call<List<FashionImageResponse>>, response: Response<List<FashionImageResponse>>) {
                if (response.isSuccessful) {
                    val ids = response.body()?.map { it.id } ?: emptyList()
                    Log.d("API", "Recent Fashion Image IDs: $ids")
                    _imageIds.postValue(ids)
                } else {
                    Log.e("API", "Failed to fetch images")
                }
            }

            override fun onFailure(call: Call<List<FashionImageResponse>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }

    private val _imageIdAll = MutableLiveData<List<Int>>()
    val imageIdAll: LiveData<List<Int>> get() = _imageIdAll

    fun getAllFashionImages() {
        apiService.getAllFashionImages().enqueue(object : Callback<List<FashionImageResponse>> {
            override fun onResponse(call: Call<List<FashionImageResponse>>, response: Response<List<FashionImageResponse>>) {
                if (response.isSuccessful) {
                    val ids = response.body()?.map { it.id } ?: emptyList()
                    Log.d("API", "All Fashion Image IDs: $ids")
                    _imageIdAll.postValue(ids)
                } else {
                    Log.e("API", "Failed to fetch images")
                }
            }

            override fun onFailure(call: Call<List<FashionImageResponse>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }


}