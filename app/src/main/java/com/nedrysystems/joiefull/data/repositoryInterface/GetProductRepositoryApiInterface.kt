package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.domain.model.GetProductApiResponse

interface GetProductRepositoryApiInterface {
    suspend fun getProduct() : List<GetProductApiResponse>
}