package com.nedrysystems.joiefull.domain.usecase.productApi

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.data.webservice.GetProductApiResponse
import javax.inject.Inject

/**
 * Use case for retrieving a list of products from the API.
 * This class encapsulates the logic for fetching products, ensuring separation of concerns
 * and adhering to clean architecture principles.
 *
 * @property getProductRepositoryInterface The repository interface used to fetch product data from the API.
 * @constructor Uses dependency injection to initialize the use case with the provided repository interface.
 */
class GetProductUseCase @Inject constructor(private val getProductRepositoryInterface: GetProductRepositoryApiInterface) {

    /**
     * Invokes the use case to fetch a list of products from the API.
     * This function is a suspend function and should be called within a coroutine or another suspend function.
     *
     * @return A list of [GetProductApiResponse] objects representing the products fetched from the API.
     */
    suspend operator fun invoke(): List<GetProductApiResponse> {
        return getProductRepositoryInterface.getProduct()
    }
}