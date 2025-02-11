package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.dao.ReviewDao
import com.nedrysystems.joiefull.data.repositoryInterface.ReviewRepositoryInterface
import com.nedrysystems.joiefull.domain.model.Review
import javax.inject.Inject

/**
 * Repository class responsible for handling the business logic related to reviews.
 * It communicates with the data layer (DAO) to perform operations on reviews and product ratings.
 * The class ensures that reviews are properly added, updated, and product ratings are maintained.
 *
 * @constructor Creates an instance of the ReviewRepository. It injects the [ReviewDao] to perform database operations.
 *
 * @property reviewDao The DAO (Data Access Object) for managing reviews in the database.
 */
class ReviewRepository @Inject constructor(private val reviewDao: ReviewDao) :
    ReviewRepositoryInterface {

    /**
     * Adds a review for a specific product by a user. If a review already exists for that product and user,
     * it updates the existing review. Otherwise, a new review is created and inserted into the database.
     *
     * @param userId The ID of the user adding the review.
     * @param productId The ID of the product being reviewed.
     * @param rate The rating given to the product (integer value).
     * @param comment The comment provided by the user for the review.
     *
     * @throws Exception If the database operation fails.
     */
    override suspend fun addReview(userId: Int, productId: Int, rate: Int, comment: String) {
        // Check if a review already exists for this user and product
        val existingReview = reviewDao.getReviewByUserAndProduct(userId, productId)

        if (existingReview != null) {
            // If a review exists, update it
            val updatedReview = existingReview.copy(rate = rate, comment = comment)
            reviewDao.insertReview(updatedReview)
        } else {
            // If no review exists, insert a new one
            val newReview = Review(
                id = 0, // ID auto-generated
                userId = userId,
                productId = productId,
                rate = rate,
                comment = comment
            )
            reviewDao.insertReview(newReview.toDto())
        }
    }

    /**
     * Updates the product's rating in the database by calling the DAO method.
     * This method is responsible for updating the product rating based on the new rate.
     *
     * @param productId The ID of the product whose rating is to be updated.
     * @param newRate The new rating for the product (integer value).
     *
     * @throws Exception If the database operation fails.
     */
    override suspend fun updateProductRating(productId: Int, newRate: Int) {
        // Call the DAO method to update the product's rating
        reviewDao.updateProductRating(productId, newRate)
    }
}

