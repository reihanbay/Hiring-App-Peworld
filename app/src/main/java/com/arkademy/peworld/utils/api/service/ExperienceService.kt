package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.ExperienceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExperienceService {
    @GET("experience/")
    suspend fun getAllExperienceById(@Query("search[idWorker]") idWorker: Int): ExperienceResponse
}