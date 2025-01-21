package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.domain.model.GetProductApiResponse

interface GetProductRepositoryInterface {
    suspend fun getProduct() : List<GetProductApiResponse>
}