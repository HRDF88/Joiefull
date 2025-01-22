package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.domain.model.ProductUiModel

interface GetProductRepositoryInterface {
    suspend fun getProducts(): List<ProductUiModel>
}