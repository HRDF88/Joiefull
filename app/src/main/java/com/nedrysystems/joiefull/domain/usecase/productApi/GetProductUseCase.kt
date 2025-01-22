package com.nedrysystems.joiefull.domain.usecase.productApi

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.domain.model.GetProductApiResponse
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val getProductRepositoryInterface: GetProductRepositoryApiInterface){

        suspend operator fun invoke(): List<GetProductApiResponse> {
            return getProductRepositoryInterface.getProduct()
        }
}