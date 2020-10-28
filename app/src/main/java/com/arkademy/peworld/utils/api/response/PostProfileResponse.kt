package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class PostProfileResponse(val success: Boolean?, val message: String?, val data: Profile) {
    data class Profile(
            @SerializedName("nameWorker") val nameWorker: String?,
            @SerializedName("jobTitle") val jobTitle: String?,
            @SerializedName("statusJob") val statusJob: String?,
            @SerializedName("city") val city: String?,
            @SerializedName("workPlace") val workPlace: String?,
            @SerializedName("description") val description: String?,
            @SerializedName("idAccount") val idAccount: Int?,
            @SerializedName("image") val image: String?
    )
}