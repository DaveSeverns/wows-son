package com.sevdotdev.wowson.di

import com.sevdotdev.data.datasource.WowDataSource
import com.sevdotdev.data_restimpl.WowDataService
import com.sevdotdev.data_restimpl.datasource.WowNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    private const val BASE_URL = "https://owen-wilson-wow-api.herokuapp.com"

    @Provides
    @Singleton
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun wowDataService(retrofit: Retrofit): WowDataService =
        retrofit.create(WowDataService::class.java)

    @Provides
    @Singleton
    fun wowDataSource(wowDataService: WowDataService): WowDataSource =
        WowNetworkDataSource(wowDataService = wowDataService)
}