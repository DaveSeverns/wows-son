package com.sevdotdev.domain.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object RunTimeFormatter {
    private val runtimePattern = Pattern.compile(RUNTIME)
    fun getRuntimeAsLong(runtime: String): Long {
        val matcher = runtimePattern.matcher(runtime)
        return if (matcher.matches()) {
            val list = runtime.split(":")
            var totalSeconds = list[2].toInt()
            totalSeconds += ((list[1].toInt()) * 60)
            totalSeconds += ((list[0].toInt()) * 3600)
            totalSeconds.toLong()
        } else {
            0L
        }
    }
}

const val RUNTIME = "^(?:(?:([01]?\\d|2[0-3]):)([0-5]?\\d):)?([0-5]?\\d)\$"