package com.nedrysystems.joiefull.domain.usecase.productApi

import com.nedrysystems.joiefull.domain.model.GetProductApiResponse
import javax.inject.Inject

class FilterProductsByCategoryUseCase @Inject constructor() {
    operator fun invoke(products: List<GetProductApiResponse>, category: String): List<GetProductApiResponse> {
        return products.filter { it.category == category }
    }
}
