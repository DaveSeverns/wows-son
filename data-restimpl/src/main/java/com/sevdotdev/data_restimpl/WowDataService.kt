package com.sevdotdev.data_restimpl

import com.sevdotdev.data_restimpl.model.WowJsonItem
import retrofit2.http.GET
import retrofit2.http.Query

typealias WowJson = List<WowJsonItem>

interface WowDataService {
    @GET(value = "/wows/random")
    suspend fun getRandomWows(@Query("results") amount: Int = 10): WowJson
}