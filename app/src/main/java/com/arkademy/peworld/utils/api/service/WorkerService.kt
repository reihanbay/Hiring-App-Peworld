package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.GetWorkerByIdResponse
import com.arkademy.peworld.utils.api.response.GetWorkerResponse
import com.arkademy.peworld.utils.api.response.PostProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface WorkerService {
    @GET("worker/")
    suspend fun getAllWorker(): GetWorkerResponse

    @GET("worker/")
    suspend fun getAllWorker(@Query("sort") sort: String?, @Query("order") order: String?): GetWorkerResponse

    @GET("worker/")
    suspend fun getAllWorker(@Query("search[jobTitle]") title: String?): GetWorkerResponse

    @GET("worker/")
    suspend fun getAllWorker(@Query("search[statusJob]") title: String?, @Query("sort") sort: String?, @Query("order") order: String? ): GetWorkerResponse

    @GET("worker/{id}")
    suspend fun getWorkerById(@Path("id") id: Int): GetWorkerByIdResponse

    @GET("worker/")
    suspend fun checkProfileById(@Query("search[idAccount]") id: Int): GetWorkerResponse

    @Multipart
    @POST("worker/")
    suspend fun postWorker(
            @Part("nameWorker") nameWorker: RequestBody?,
            @Part("jobTitle") jobTitle: RequestBody?,
            @Part("statusJob") statusJob: RequestBody?,
            @Part("city") city: RequestBody?,
            @Part("workPlace") workPlace: RequestBody?,
            @Part("description") description: RequestBody?,
            @Part("idAccount") idAccount: RequestBody?,
            @Part image: MultipartBody.Part?
    ): PostProfileResponse

    @Multipart
    @PATCH("worker/{id}")
    suspend fun patchWorker(
            @Path("id") id: Int?,
            @Part("nameWorker") nameWorker: RequestBody?,
            @Part("jobTitle") jobTitle: RequestBody?,
            @Part("statusJob") statusJob: RequestBody?,
            @Part("city") city: RequestBody?,
            @Part("workPlace") workPlace: RequestBody?,
            @Part("description") description: RequestBody?,
            @Part image: MultipartBody.Part?
    ): PostProfileResponse
}