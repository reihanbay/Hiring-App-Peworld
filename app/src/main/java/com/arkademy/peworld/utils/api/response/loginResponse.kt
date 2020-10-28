package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class loginResponse(val success: Boolean, val messages: String, val data: DataResult) {
    data class DataResult(
        @SerializedName("idAccount") val id: Int?,
        @SerializedName("email") val email: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("password") val password: String?,
        @SerializedName("status") val status: String?,
        @SerializedName("role") val role: String?,
        @SerializedName("token") val token: String?
    )
}