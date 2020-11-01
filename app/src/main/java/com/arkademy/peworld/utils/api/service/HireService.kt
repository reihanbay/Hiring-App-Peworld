package com.arkademy.peworld.utils.api.service

import com.arkademy.peworld.utils.api.response.GetHireByIdResponse
import com.arkademy.peworld.utils.api.response.GetHireResponse
import com.arkademy.peworld.utils.api.response.PatchHireResponse
import retrofit2.http.*

interface HireService {
    @GET("hire/")
    suspend fun getHire(@Query("search[idWorker]") id : Int): GetHireResponse

    @GET("hire/{id}")
    suspend fun getHireById(@Path("id") id : Int): GetHireByIdResponse

    @FormUrlEncoded
    @PATCH("hire/{id}")
    suspend fun patchConfirm(
            @Path("id") idHire: Int,
            @Field("statusConfirm") statusConfirm: String?,
    ) : PatchHireResponse
}