package com.nedrysystems.joiefull.domain.usecase.productLocal

import android.util.Log
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import javax.inject.Inject

/**
 * Use case for updating the favorite status of a product.
 * This class encapsulates the logic for updating whether a product is marked as a favorite in the local repository,
 * ensuring separation of concerns and adherence to clean architecture principles.
 *
 * @property getProductLocalRepositoryInterface The repository interface used to update the favorite status in the local database.
 * @constructor Uses dependency injection to initialize the use case with the provided local repository interface.
 */
class UpdateFavoriteStatusUseCase @Inject constructor(
    private val getProductLocalRepositoryInterface: GetProductLocalRepositoryInterface
) {

    /**
     * Executes the use case to update the favorite status of a product.
     * This function is a suspend function and should be called within a coroutine or another suspend function.
     *
     * @param id The ID of the product whose favorite status is to be updated.
     * @param favorite A boolean indicating whether the product is marked as a favorite.
     */
    suspend fun execute(id: Int, favorite: Boolean) {
        Log.d("FavoriteStatusUseCase", "Mise à jour du produit ID: $id, Favori: $favorite")
        getProductLocalRepositoryInterface.updateFavoriteStatus(id, favorite)
        Log.d("FavoriteStatusUseCase", "Produit avec ID $id a été mis à jour dans la base de données")
    }
}
