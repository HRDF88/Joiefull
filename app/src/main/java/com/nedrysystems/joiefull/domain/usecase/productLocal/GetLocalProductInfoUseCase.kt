package com.nedrysystems.joiefull.domain.usecase.productLocal

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

class GetLocalProductInfoUseCase @Inject constructor(private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface){
    suspend fun execute(): List<ProductLocalInfo>{
        return getProductLocalRepositoryInterface.getLocalProductInfo()
    }
}