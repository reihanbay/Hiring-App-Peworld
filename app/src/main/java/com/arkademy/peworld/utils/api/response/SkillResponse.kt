package com.arkademy.peworld.utils.api.response

import com.google.gson.annotations.SerializedName

class SkillResponse(val success: Boolean?, val message: String?, val data: Skill) {
    data class Skill(
        @SerializedName("idSkill") val idSkill: Int?,
        @SerializedName("idWorker") val idWorker: Int?,
        @SerializedName("skill") val skill: String?,
    )
}