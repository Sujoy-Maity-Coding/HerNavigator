package com.sujoy.hernavigator.Api.api.auth_api

import com.sujoy.hernavigator.Api.data.AuthDataModel.CreateUserItem
import com.sujoy.hernavigator.Api.data.AuthDataModel.UserResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HTTP
import retrofit2.http.POST


interface API_Builder {
    @FormUrlEncoded
    @POST("createUser")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone_number") phone: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>
}