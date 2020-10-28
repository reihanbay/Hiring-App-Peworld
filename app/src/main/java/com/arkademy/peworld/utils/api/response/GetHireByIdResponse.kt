package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class GetHireByIdResponse(val success: Boolean, val message: String, val data: Hire) {
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
        @SerializedName("nameCompany") val recruiter: String?,
        @SerializedName("image") val image: String?,
        @SerializedName("nameProject") val nameProject: String?,
        @SerializedName("deadline") val deadline: String?,
    )
}