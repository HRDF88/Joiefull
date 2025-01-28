package com.nedrysystems.joiefull.domain.usecase.productLocal

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

/**
 * Use case for retrieving a list of local product information.
 * This class encapsulates the logic for fetching locally stored product data,
 * ensuring separation of concerns and adhering to clean architecture principles.
 *
 * @property getProductLocalRepositoryInterface The repository interface used to fetch local product data.
 * @constructor Uses dependency injection to initialize the use case with the provided local repository interface.
 */
class GetLocalProductInfoUseCase @Inject constructor(private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface) {

    /**
     * Executes the use case to retrieve a list of local product information.
     * This function is a suspend function and should be called within a coroutine or another suspend function.
     *
     * @return A list of [ProductLocalInfo] objects representing the locally stored product information.
     */
    suspend fun execute(): List<ProductLocalInfo> {
        return getProductLocalRepositoryInterface.getLocalProductInfo()
    }
}