package com.nedrysystems.joiefull.di

import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import com.nedrysystems.joiefull.utils.image.library.GlideUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {
    @Provides
    @Singleton
    fun provideImageLoader(): ImageLoader {
        return GlideUtils
    }
}
