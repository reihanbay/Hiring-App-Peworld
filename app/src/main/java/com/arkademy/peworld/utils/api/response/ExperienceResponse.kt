package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class ExperienceResponse(
    val success: Boolean?,
    val message: String?,
    val data: List<Experience>
) {
    data class Experience(
        @SerializedName("idExperience") val idExperience: Int,
        @SerializedName("companyName") val companyName: String,
        @SerializedName("description") val description: String,
        @SerializedName("workPosition") val workPosition: String,
        @SerializedName("start") val start: String,
        @SerializedName("end") val end: String,
        @SerializedName("idWorker") val idWorker: Int,
    )
}