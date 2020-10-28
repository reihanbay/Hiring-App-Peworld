package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class PortfolioResponse(
    val success: Boolean?,
    val message: String?,
    val data: List<Portfolio>
) {
    data class Portfolio(
        @SerializedName("idPortfolio") val idPortfolio: Int,
        @SerializedName("namePortfolio") val namePortfolio: String,
        @SerializedName("linkRepository") val linkRepo: String,
        @SerializedName("typePortfolio") val typePortfolio: String,
        @SerializedName("image") val imagePortfolio: String,
        @SerializedName("idWorker") val idWorker: Int,
    )
}