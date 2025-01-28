package com.nedrysystems.joiefull.domain.usecase.productLocal

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

/**
 * Use case for inserting or updating local product information.
 * This class encapsulates the logic for either adding a new product's data
 * or updating the existing data in the local repository, ensuring clean architecture principles.
 *
 * @property getProductLocalRepositoryInterface The repository interface used to insert or update local product data.
 * @constructor Uses dependency injection to initialize the use case with the provided local repository interface.
 */
class InsertOrUpdateProductLocalInfoUseCase @Inject constructor(
    private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface
) {

    /**
     * Executes the use case to insert or update product local information.
     * This function is a suspend function and should be called within a coroutine or another suspend function.
     *
     * @param productLocalInfo The product's local information to be inserted or updated.
     */
    suspend fun execute(productLocalInfo: ProductLocalInfo) {
        getProductLocalRepositoryInterface.insertOrUpdateProductLocalInfo(productLocalInfo)
    }
}
