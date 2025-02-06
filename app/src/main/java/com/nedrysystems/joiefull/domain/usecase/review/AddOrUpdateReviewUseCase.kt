package com.nedrysystems.joiefull.domain.usecase.review

import com.nedrysystems.joiefull.data.repositoryInterface.ReviewRepositoryInterface
import javax.inject.Inject

/**
 * Use case for adding or updating a review for a product.
 * This class is responsible for handling the logic to either insert a new review or update
 * an existing review for a specific product by a user.
 *
 * @property reviewRepository The repository interface for interacting with the review data.
 */
class AddOrUpdateReviewUseCase @Inject constructor(private val reviewRepository: ReviewRepositoryInterface) {

    /**
     * Executes the use case to add or update a review for a product.
     *
     * This method first checks if a review already exists for the given user and product.
     * If a review exists, it updates the existing one; otherwise, it adds a new review.
     *
     * @param userId The unique identifier of the user submitting the review.
     * @param productId The unique identifier of the product being reviewed.
     * @param rate The rating given by the user for the product (usually between 1 and 5).
     * @param comment The text comment left by the user about the product.
     */
    suspend fun execute(userId: Int, productId: Int, rate: Int, comment: String) {
        reviewRepository.addReview(userId, productId, rate, comment)
    }
}
