package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class GetHireResponse(val success: Boolean, val message: String, val data: List<Hire>) {
    data class Hire(
            @SerializedName("idHire") val idHire: Int?,
            @SerializedName("projectJob") val projectJob: String?,
            @SerializedName("message") val message: String?,
            @SerializedName("statusConfirm") val statusConfirm: String?,
            @SerializedName("dateConfirm") val dateConfirm: String?,
            @SerializedName("price") val price: Int?,
            @SerializedName("idWorker") val idWorker: Int?,
            @SerializedName("idProject") val idProject: Int?,
            @SerializedName("createdAt") val createdAt: String?,
            @SerializedName("updatedAt") val updatedAt: String?,
            @SerializedName("recruiter") val recruiter: String?,
            @SerializedName("image") val image: String?,
    )
}