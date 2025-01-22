package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.data.entity.ProductEntity
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo

interface GetProductLocalRepositoryInterface {

    suspend fun getLocalProductInfo(): List<ProductLocalInfo>

    suspend fun getProductLocalInfo(id: Int): ProductLocalInfo?

    suspend fun insertOrUpdateProductLocalInfo(productLocalInfo: ProductLocalInfo)

    suspend fun updateFavoriteStatus(id: Int, favorite: Boolean)
}