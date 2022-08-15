package com.sevdotdev.data.datasource

import com.sevdotdev.domain.model.WowMetaData

interface WowDataSource {
    suspend fun getWowsMetaData(): Result<List<WowMetaData>>
}