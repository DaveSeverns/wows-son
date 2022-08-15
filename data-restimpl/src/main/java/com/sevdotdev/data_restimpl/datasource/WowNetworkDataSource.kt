package com.sevdotdev.data_restimpl.datasource

import com.sevdotdev.data.datasource.WowDataSource
import com.sevdotdev.data_restimpl.WowDataService
import com.sevdotdev.data_restimpl.mapper.toDomainList
import com.sevdotdev.domain.model.WowMetaData

class WowNetworkDataSource(private val wowDataService: WowDataService): WowDataSource {
    override suspend fun getWowsMetaData(): Result<List<WowMetaData>> {
        return try {
            Result.success(wowDataService.getRandomWows().toDomainList())
        } catch (ex: Throwable) {
            Result.failure(exception = ex)
        }
    }
}