package com.nedrysystems.joiefull.di

import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.data.webservice.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module that provides network-related dependencies for API services.
 * This module is installed in the [SingletonComponent], meaning its dependencies are available application-wide.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides an implementation of [GetProductApiService].
     * Uses the [RetrofitClient] to initialize and return the API service instance.
     *
     * @return An instance of [GetProductApiService] for making API calls.
     */
    @Provides
    fun provideGetProductApiService(): GetProductApiService {
        return RetrofitClient.getProductApiService
    }
}