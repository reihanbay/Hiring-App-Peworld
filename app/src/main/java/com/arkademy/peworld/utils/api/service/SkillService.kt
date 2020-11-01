package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.ListSkillResponse
import com.arkademy.peworld.utils.api.response.SkillResponse
import retrofit2.http.*

interface SkillService {

    @GET("skill/")
    suspend fun getSkill(@Query("search[idWorker]") id: Int?) : ListSkillResponse

    @FormUrlEncoded
    @POST("skill/")
    suspend fun postSkill(@Field("idWorker") id: Int?,
    @Field("skill") skill: String?
    ) : SkillResponse

    @DELETE("skill/{id}")
    suspend fun deleteSkill(@Path("id") id: Int?,
    ) : SkillResponse
}