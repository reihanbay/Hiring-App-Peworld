package com.arkademy.peworld.utils.model

data class ExperienceModel(
    val idExperience: Int,
    val companyName: String,
    val description: String,
    val workPosition: String,
    val start: String,
    val end: String,
    val idWorker: Int
)