package com.nedrysystems.joiefull.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.database.AppDatabase
import com.nedrysystems.joiefull.data.repository.GetProductApiRepositoryApi
import com.nedrysystems.joiefull.data.repository.GetProductLocalRepository
import com.nedrysystems.joiefull.data.repository.GetProductRepository
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryInterface
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

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)  // Appel avec uniquement le Context
    }

    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao() // Appel au DAO depuis la base de données
    }

    @Provides
    @Singleton
    fun provideGetProductApiRepository(getProductApiService: GetProductApiService): GetProductRepositoryApiInterface {
        return GetProductApiRepositoryApi(getProductApiService)
    }

    @Provides
    @Singleton
    fun provideGetProductLocalRepository(productDao: ProductDao): GetProductLocalRepositoryInterface {
        return GetProductLocalRepository(productDao)
    }

    @Provides
    @Singleton
    fun provideGetProductRepository(
        getProductApiRepository: GetProductRepositoryApiInterface,
        getProductLocalRepository: GetProductLocalRepositoryInterface
    ): GetProductRepositoryInterface {
        return GetProductRepository(getProductApiRepository, getProductLocalRepository)
    }

    @Module
    @InstallIn(ViewModelComponent::class) // Assurez-vous que vous utilisez le bon scope
    abstract class ViewModelModule {

        @Binds
        abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
    }
}
