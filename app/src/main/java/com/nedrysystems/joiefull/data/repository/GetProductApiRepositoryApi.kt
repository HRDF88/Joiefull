package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.domain.model.GetProductApiResponse
import javax.inject.Inject

class GetProductApiRepositoryApi @Inject constructor(private val getProductApiService: GetProductApiService) :
    GetProductRepositoryApiInterface {

    override suspend fun getProduct(): List<GetProductApiResponse> {
        return getProductApiService.getProduct()
    }
}