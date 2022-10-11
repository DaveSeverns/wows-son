package com.sevdotdev.data_restimpl

import com.sevdotdev.data_restimpl.model.WowJsonItem
import retrofit2.http.GET
import retrofit2.http.Query

typealias WowJson = List<WowJsonItem>

const val RANDOM_WOW_ENDPOINT = "/wows/random"

interface WowDataService {
    @GET(value = RANDOM_WOW_ENDPOINT)
    suspend fun getRandomWows(
        @Query("results") amount: Int? = 10,
        @Query("movie") movieTitle: String? = null,
        @Query("wow_in_movie") wowInMovie: String? = null,
    ): WowJson
}