package com.nedrysystems.joiefull.di

import com.nedrysystems.joiefull.utils.image.imageInterface.ImageLoader
import com.nedrysystems.joiefull.utils.image.library.GlideUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module that provides an image loader dependency.
 * This module is installed in the [SingletonComponent], meaning its dependencies are available application-wide.
 */
@Module
@InstallIn(SingletonComponent::class)
object ImageLoaderModule {

    /**
     * Provides a singleton instance of [ImageLoader].
     * Uses [GlideUtils] as the implementation of the image loader.
     *
     * @return A singleton instance of [ImageLoader].
     */
    @Provides
    @Singleton
    fun provideImageLoader(): ImageLoader {
        return GlideUtils
    }
}
