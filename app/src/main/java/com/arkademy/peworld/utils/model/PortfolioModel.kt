package com.arkademy.peworld.utils.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PortfolioModel(
        val idPortfolio: Int,
        val image: String,
        val linkRepo: String,
        val typePortfolio: String,
        val namePortfolio: String,
        val idWorker: Int,
) : Parcelable