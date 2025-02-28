package com.sujoy.hernavigatoradmin.Presentation.ViewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sujoy.hernavigatoradmin.api.RetrofitClient
import com.sujoy.hernavigatoradmin.model.FashionImageResponse
import com.sujoy.hernavigatoradmin.model.Notification
import com.sujoy.hernavigatoradmin.model.UploadResponse
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

    // Add Notification
    private val _notificationResult = MutableLiveData<Boolean>()
    val notificationResult: LiveData<Boolean> = _notificationResult

    fun addNotification(notificationText: String) {
        val notification = mapOf("notification" to notificationText)
        apiService.addNotification(notification).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Log.d("API", "Notification Added: ${response.body()}")
                    _notificationResult.postValue(true) // Notify success
                } else {
                    Log.e("API", "Failed: ${response.errorBody()?.string()}")
                    _notificationResult.postValue(false) // Notify failure
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
                _notificationResult.postValue(false) // Notify failure
            }
        })
    }

    // Get Notifications
    fun getNotifications() {
        apiService.getNotifications().enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
                if (response.isSuccessful) {
                    _notifications.postValue(response.body() ?: emptyList())
                } else {
                    Log.e("API", "Failed to get notifications")
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }

    // Upload Fashion Image
    fun uploadFashionImage(imageFile: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)
        val body = MultipartBody.Part.createFormData("photo", imageFile.name, requestFile)

        apiService.uploadFashionImage(body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    _uploadStatus.postValue("Upload Successful: ${response.body()?.message}")
                } else {
                    _uploadStatus.postValue("Upload Failed")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _uploadStatus.postValue("Error: ${t.message}")
            }
        })
    }

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


}

