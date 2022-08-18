package com.sevdotdev.domain.model

data class WowContent(
    val audioUrl: String,
    val videoContent: VideoContent,
)

data class VideoContent(
    val highDefOptions: List<VideoOption.HighDef>,
    val standardDefOptions: List<VideoOption.StandardDef>,
)

sealed interface VideoOption {
    val url: String

    data class HighDef(
        override val url: String,
        val resolution: Resolution.HD = Resolution.HD.SEVEN_20,
    ) : VideoOption

    data class StandardDef(
        override val url: String,
        val resolution: Resolution.SD = Resolution.SD.FOUR_80,
    ) : VideoOption
}

sealed interface Resolution {
    enum class HD : Resolution {
        FOUR_K,
        TEN_80,
        SEVEN_20,
    }

    enum class SD : Resolution {
        FOUR_80,
        THREE_60,
    }
}
