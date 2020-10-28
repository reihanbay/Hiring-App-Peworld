package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class PatchHireResponse(val success: Boolean?, val message: String?, val data: Hire?) {
    data class Hire(
            @SerializedName("idHire") val idHire: Int?,
            @SerializedName("statusConfirm") val statusConfirm: String?,
//            @SerializedName("dateConfirm") val dateConfirm: String?,
    )
}