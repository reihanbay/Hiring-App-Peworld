package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.RegisterResponse
import com.arkademy.peworld.utils.api.response.loginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountService {
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("email") email: String,
                      @Field("password") password: String): loginResponse

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("noHp") noHp: String,
            @Field("status") status: Int,
            @Field("role") role: String,
    ) : RegisterResponse
}