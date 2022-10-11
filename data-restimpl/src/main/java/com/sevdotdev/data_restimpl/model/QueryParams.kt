package com.sevdotdev.data_restimpl.model

import com.sevdotdev.data_restimpl.mapper.DELIMITER

internal data class QueryParams(val title: String, val wowInMovie: String) {
    object Builder {
        private var title = ""
        private var wowInMovie = ""

        fun addTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun addWowInMovie(wowInMovie: String): Builder {
            this.wowInMovie = wowInMovie
            return this
        }

        fun build() = QueryParams(title, wowInMovie)
    }
}

internal fun String.toParams(): QueryParams {
    val list = this.split(DELIMITER)
    val builder = QueryParams.Builder
    return try {
        builder
            .addTitle(list[0])
            .addWowInMovie(list[1])
            .build()
    } catch (ex: Exception) {
        builder.build()
    }
}
