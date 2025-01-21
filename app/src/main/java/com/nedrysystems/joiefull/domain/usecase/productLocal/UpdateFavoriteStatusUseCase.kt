package com.nedrysystems.joiefull.domain.usecase.productLocal

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import javax.inject.Inject

class UpdateFavoriteStatusUseCase @Inject constructor(
    private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface
) {
    suspend fun execute(id: Int, favorite: Boolean) {
        getProductLocalRepositoryInterface.updateFavoriteStatus(id, favorite)
    }
}
