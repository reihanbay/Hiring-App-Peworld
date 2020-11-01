package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.DeleteResponse
import com.arkademy.peworld.utils.api.response.ExperienceResponse
import com.arkademy.peworld.utils.api.response.ListExperienceResponse
import retrofit2.http.*

interface ExperienceService {
    @GET("experience/")
    suspend fun getAllExperienceById(@Query("search[idWorker]") idWorker: Int): ListExperienceResponse

    @FormUrlEncoded
    @POST("experience/")
    suspend fun postExperience(
        @Field("companyName") companyName: String?,
        @Field("description") description: String?,
        @Field("workPosition") workPosition: String?,
        @Field("start") start: String?,
        @Field("end") end: String?,
        @Field("idWorker") idWorker: Int?,
    ): ExperienceResponse

    @FormUrlEncoded
    @PATCH("experience/{id}")
    suspend fun patchExperience(
        @Path("id") id: Int?,
        @Field("companyName") companyName: String?,
        @Field("description") description: String?,
        @Field("workPosition") workPosition: String?,
        @Field("start") start: String?,
        @Field("end") end: String?,
        @Field("idWorker") idWorker: Int?,
    ): ExperienceResponse

    @FormUrlEncoded
    @DELETE("experience/")
    suspend fun deleteExperience(@Path("id") id: Int?) : DeleteResponse

}