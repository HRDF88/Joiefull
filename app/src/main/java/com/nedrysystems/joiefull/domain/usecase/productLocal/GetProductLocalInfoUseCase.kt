package com.nedrysystems.joiefull.domain.usecase.productLocal

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

class GetProductLocalInfoUseCase @Inject constructor(
    private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface
) {
    suspend fun execute(id: Int): ProductLocalInfo? {
        return getProductLocalRepositoryInterface.getProductLocalInfo(id)
    }
}
