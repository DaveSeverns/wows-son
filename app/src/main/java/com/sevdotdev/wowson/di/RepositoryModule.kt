package com.sevdotdev.wowson.di

import com.sevdotdev.data.datasource.WowDataSource
import com.sevdotdev.data.repository.WowRepository
import com.sevdotdev.data.repository.WowRepositoryBaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun wowDataRepository(dataSource: WowDataSource): WowRepository =
        WowRepositoryBaseImpl(wowDataSource = dataSource)
}