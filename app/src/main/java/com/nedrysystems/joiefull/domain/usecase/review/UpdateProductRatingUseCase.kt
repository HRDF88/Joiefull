package com.nedrysystems.joiefull.domain.usecase.review

import com.nedrysystems.joiefull.data.repositoryInterface.ReviewRepositoryInterface
import javax.inject.Inject

/**
 * Use case for updating the rating of a product.
 * This class handles the logic to update the product's rating in the repository.
 *
 * @property reviewRepository The repository interface for interacting with the review data.
 */
class UpdateProductRatingUseCase @Inject constructor(private val reviewRepository: ReviewRepositoryInterface) {

    /**
     * Executes the use case to update the rating of a product.
     *
     * This method calls the repository to update the rating of the specified product.
     *
     * @param productId The unique identifier of the product whose rating is to be updated.
     * @param newRate The new rating to be assigned to the product (usually between 1 and 5).
     */
    suspend fun execute(productId: Int, newRate: Int) {
        reviewRepository.updateProductRating(productId, newRate)
    }
}
