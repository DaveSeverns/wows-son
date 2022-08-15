package com.sevdotdev.data.repository

import com.sevdotdev.domain.model.WowMetaData
import kotlinx.coroutines.flow.Flow

typealias WowDataFlow = Flow<Result<List<WowMetaData>>>
interface WowRepository{
    suspend fun getWowsData(): WowDataFlow
}