package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.PortfolioResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PortfolioService {
    @GET("portofolio/")
    suspend fun getAllPortfolioById(@Query("search[idWorker]") idWorker: Int): PortfolioResponse
}