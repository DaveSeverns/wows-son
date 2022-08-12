package com.sevdotdev.domain.model

data class WowContent(
    val audioUrl: String,
    val videoContent: VideoContent,
)

data class VideoContent(
    val highDefOptions: List<Definition.HighDef>,
    val standardDefOptions: List<Definition.StandardDef>,
)

sealed interface Definition {
    val url: String

    data class HighDef(override val url: String) : Definition
    data class StandardDef(override val url: String) : Definition

    companion object {
        val highDefOptions = listOf(Resolution.FOUR_K, Resolution.TEN_80, Resolution.SEVEN_20)
        val standardDefOptions = listOf(Resolution.FOUR_80, Resolution.THREE_60)
    }
}

enum class Resolution {
    FOUR_K,
    TEN_80,
    SEVEN_20,
    FOUR_80,
    THREE_60,
}