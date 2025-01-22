package com.nedrysystems.joiefull.domain.usecase.product

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductUiModel
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val getProductRepositoryInterface: GetProductRepositoryInterface) {
    suspend fun execute(): List<ProductUiModel> {
        return getProductRepositoryInterface.getProducts()
    }
}