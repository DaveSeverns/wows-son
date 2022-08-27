package com.sevdotdev.data.repository

import com.sevdotdev.data.datasource.WowDataSource
import kotlinx.coroutines.flow.flowOf


class WowRepositoryBaseImpl(private val wowDataSource: WowDataSource) : WowRepository {
    override suspend fun getRandomWows(): WowDataListFlow {
        return flowOf(wowDataSource.getWowsMetaData())
    }

    override suspend fun getWowById(wowId: String): WowDataFlow {
        return flowOf(wowDataSource.getWowMetaDataById(wowId))
    }
}