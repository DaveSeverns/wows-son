package com.sevdotdev.data_restimpl.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Video(
    @Json(name = "1080p")
    val highDefLarge: String,
    @Json(name = "720p")
    val highDefSmall: String,
    @Json(name = "480p")
    val standardDef: String,
    @Json(name = "360p")
    val lowDef: String
)