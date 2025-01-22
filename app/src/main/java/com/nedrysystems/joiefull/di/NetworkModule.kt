package com.nedrysystems.joiefull.di

import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.data.webservice.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt NetworkModule for Api Class.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideGetProductApiService() : GetProductApiService {
        return RetrofitClient.getProductApiService
    }
}