package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(val success: Boolean, val message: String, val data: DataResult) {
    data class DataResult(
            @SerializedName("id") val id: Int?,
            @SerializedName("name") val name: String?,
            @SerializedName("email") val email: String?,
            @SerializedName("noHp") val noHp: String?,
            @SerializedName("role") val role: String?,
            @SerializedName("status") val status: Int?,
    )
}