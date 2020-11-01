package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.DeleteResponse
import com.arkademy.peworld.utils.api.response.EditPortfolioResponse
import com.arkademy.peworld.utils.api.response.PortfolioResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PortfolioService {
    @GET("portofolio/")
    suspend fun getAllPortfolioById(@Query("search[idWorker]") idWorker: Int): PortfolioResponse

    @Multipart
    @POST("portofolio/")
    suspend fun postPortfolio(
        @Part("namePortfolio") name: RequestBody?,
        @Part("linkRepository") link: RequestBody?,
        @Part("typePortfolio") type: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("idWorker") idWorker: RequestBody?
    ): EditPortfolioResponse

    @Multipart
    @PATCH("portofolio/{id}")
    suspend fun patchPortfolio(
        @Path("id") idPortfolio :Int?,
        @Part("namePortfolio") name: RequestBody?,
        @Part("linkRepository") link: RequestBody?,
        @Part("typePortfolio") type: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("idWorker") idWorker: RequestBody?
    ): EditPortfolioResponse

    @DELETE("portofolio/{id}")
    suspend fun deletePortfolio(@Path("id") id: Int?) : DeleteResponse
}