package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

data class ListSkillResponse(val success: Boolean?, val messages: String?, val data: List<Skill>) {
    data class Skill(
        @SerializedName("idSkill") val idSkill: Int?,
        @SerializedName("idWorker") val idWorker: Int?,
        @SerializedName("skill") val skill: String?,
    )
}