package com.nedrysystems.joiefull.domain.usecase.productApi

import com.nedrysystems.joiefull.data.webservice.GetProductApiResponse
import javax.inject.Inject

/**
 * Use case for filtering a list of products by a specific category.
 * This class is designed to encapsulate the logic for filtering products
 * to ensure separation of concerns and maintain clean architecture principles.
 *
 * @constructor Uses dependency injection to create an instance of the use case.
 */
class FilterProductsByCategoryUseCase @Inject constructor() {

    /**
     * Filters a list of products based on the given category.
     *
     * @param products The list of [GetProductApiResponse] objects to be filtered.
     * @param category The category by which the products should be filtered.
     * @return A list of [GetProductApiResponse] objects that match the specified category.
     */
    operator fun invoke(
        products: List<GetProductApiResponse>,
        category: String
    ): List<GetProductApiResponse> {
        return products.filter { it.category == category }
    }
}
