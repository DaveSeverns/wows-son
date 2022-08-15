package com.sevdotdev.data.repository

import com.sevdotdev.data.datasource.WowDataSource
import kotlinx.coroutines.flow.flowOf


class WowRepositoryBaseImpl(private val wowDataSource: WowDataSource) : WowRepository {
    override suspend fun getWowsData(): WowDataFlow {
        return flowOf(wowDataSource.getWowsMetaData())
    }
}