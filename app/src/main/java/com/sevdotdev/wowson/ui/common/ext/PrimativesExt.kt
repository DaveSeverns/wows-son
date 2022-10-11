package com.sevdotdev.wowson.ui.common.ext

fun Int.countSuffix(): String {

    val thisAsString = this.toString()
    return when {
        this.isSpecialCase() -> {
            thisAsString + NTH
        }
        else -> {
            when (thisAsString.last()) {
                '1' -> {
                    thisAsString + FIRST
                }
                '2' -> {
                    thisAsString + SECOND
                }
                '3' -> {
                    thisAsString + THIRD
                }
                else -> {
                    thisAsString + NTH
                }
            }
        }
    }

}

private fun Int.isSpecialCase(): Boolean {
    return when (this) {
        ELEVEN, TWELVE, THIRTEEN -> {
            true
        }
        else -> {
            false
        }
    }
}

const val FIRST = "st"
const val SECOND = "nd"
const val THIRD = "rd"
const val NTH = "th"
const val ELEVEN = 11
const val TWELVE = 12
const val THIRTEEN = 13