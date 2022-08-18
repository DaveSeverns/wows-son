package com.sevdotdev.domain.utils

import org.junit.Assert.assertEquals
import org.junit.Test


internal class RunTimeFormatterTest {

    private val secondsControl = 1L
    private val minutesControl = 60L
    private val hoursControl = 3600L

    @Test
    fun `seconds runtime should return correct number`() {
        val runtimeString = "00:00:01"
        val seconds = RunTimeFormatter.getRuntimeAsLong(runtimeString)
        assertEquals(secondsControl, seconds)
    }

    @Test
    fun `minutes runtime should return correct number`() {
        val runtimeString = "00:01:00"
        val seconds = RunTimeFormatter.getRuntimeAsLong(runtimeString)
        assertEquals(minutesControl, seconds)
    }

    @Test
    fun `hour runtime should return correct number`() {
        val runtimeString = "01:00:00"
        val seconds = RunTimeFormatter.getRuntimeAsLong(runtimeString)
        assertEquals(hoursControl, seconds)
    }

    @Test
    fun `varied runtime should return correct number`() {
        val runtime = "02:12:22"
        val seconds = RunTimeFormatter.getRuntimeAsLong(runtime)
        val control = (2 * hoursControl) + (12 * minutesControl) + (22 * secondsControl)
        assertEquals(control, seconds)
    }

    @Test
    fun `incorrect format returns 0`() {
        val runtimeIncorrect = "02:929:29"
        val seconds = RunTimeFormatter.getRuntimeAsLong(runtimeIncorrect)
        assertEquals(0L, seconds)
    }
}