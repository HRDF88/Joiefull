package com.nedrysystems.joiefull.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.database.AppDatabase
import com.nedrysystems.joiefull.data.repository.GetProductApiRepositoryApi
import com.nedrysystems.joiefull.data.repository.GetProductLocalRepository
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Dagger module that provides dependencies for the application.
 * This module is installed in the [SingletonComponent], meaning its dependencies are available application-wide.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Provides a [CoroutineScope] with a [SupervisorJob] and the [Dispatchers.Main] context.
     * This scope is suitable for managing coroutines with a lifecycle independent of individual jobs.
     *
     * @return A singleton instance of [CoroutineScope].
     */
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Provides the application-wide instance of [AppDatabase].
     * Uses the context to initialize the database if it is not already created.
     *
     * @param context The application context used to initialize the database.
     * @return A singleton instance of [AppDatabase].
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)  // Appel avec uniquement le Context
    }

    /**
     * Provides the [ProductDao] instance from the [AppDatabase].
     *
     * @param appDatabase The [AppDatabase] instance from which the DAO is retrieved.
     * @return A singleton instance of [ProductDao].
     */
    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao() // Appel au DAO depuis la base de données
    }

    /**
     * Provides an implementation of [GetProductRepositoryApiInterface].
     * Uses the [GetProductApiService] to initialize the [GetProductApiRepositoryApi].
     *
     * @param getProductApiService The API service used to fetch product data from the remote API.
     * @return A singleton instance of [GetProductRepositoryApiInterface].
     */
    @Provides
    @Singleton
    fun provideGetProductApiRepository(getProductApiService: GetProductApiService): GetProductRepositoryApiInterface {
        return GetProductApiRepositoryApi(getProductApiService)
    }

    /**
     * Provides an implementation of [GetProductLocalRepositoryInterface].
     * Uses the [ProductDao] to initialize the [GetProductLocalRepository].
     *
     * @param productDao The DAO used for interacting with the local database.
     * @return A singleton instance of [GetProductLocalRepositoryInterface].
     */
    @Provides
    @Singleton
    fun provideGetProductLocalRepository(productDao: ProductDao): GetProductLocalRepositoryInterface {
        return GetProductLocalRepository(productDao)
    }

    /**
     * Dagger module that provides dependencies for ViewModels.
     * This module is installed in the [ViewModelComponent], meaning its dependencies are available in the ViewModel scope.
     */
    @Module
    @InstallIn(ViewModelComponent::class) // Assurez-vous que vous utilisez le bon scope
    abstract class ViewModelModule {

        /**
         * Binds the [HomeViewModel] as a [ViewModel] in the ViewModel scope.
         * Ensures that [HomeViewModel] is provided wherever a [ViewModel] dependency is required.
         *
         * @param homeViewModel The implementation of the [HomeViewModel].
         */
        @Binds
        abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
    }
}
