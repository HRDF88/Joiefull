package com.nedrysystems.joiefull.domain.usecase.productLocal

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

/**
 * Use case for retrieving local product information by its ID.
 * This class encapsulates the logic for fetching a single product's local data
 * from the repository, ensuring clean architecture principles and separation of concerns.
 *
 * @property getProductLocalRepositoryInterface The repository interface used to fetch local product data.
 * @constructor Uses dependency injection to initialize the use case with the provided local repository interface.
 */
class GetProductLocalInfoUseCase @Inject constructor(
    private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface
) {

    /**
     * Executes the use case to retrieve local product information by its ID.
     * This function is a suspend function and should be called within a coroutine or another suspend function.
     *
     * @param id The ID of the product whose local information is to be fetched.
     * @return A [ProductLocalInfo] object containing the local product data, or null if no product is found with the given ID.
     */
    suspend fun execute(id: Int): ProductLocalInfo? {
        return getProductLocalRepositoryInterface.getProductLocalInfo(id)
    }
}
