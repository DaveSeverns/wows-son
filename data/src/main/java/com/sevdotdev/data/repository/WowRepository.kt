package com.sevdotdev.data.repository

import com.sevdotdev.domain.model.WowMetaData
import kotlinx.coroutines.flow.Flow

typealias WowDataListFlow = Flow<Result<List<WowMetaData>>>
typealias WowDataFlow = Flow<Result<WowMetaData>>
interface WowRepository{
    suspend fun getRandomWows(): WowDataListFlow
    suspend fun getWowById(wowId: String): WowDataFlow
}