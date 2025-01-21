package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryInterface
import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.domain.model.GetProductApiResponse
import javax.inject.Inject

class GetProductRepository @Inject constructor(private val getProductApiService: GetProductApiService) :
    GetProductRepositoryInterface {

    override suspend fun getProduct(): List<GetProductApiResponse> {
        return getProductApiService.getProduct()
    }
}