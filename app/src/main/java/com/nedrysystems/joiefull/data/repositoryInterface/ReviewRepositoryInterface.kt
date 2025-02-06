package com.nedrysystems.joiefull.data.repositoryInterface

/**
 * Interface for the Review Repository.
 * This interface defines the methods required for interacting with reviews and updating product ratings.
 * It provides methods for adding a review and updating the rating of a product.
 *
 * @see ReviewRepository The implementation of this interface.
 */
interface ReviewRepositoryInterface {

    /**
     * Adds a new review for a specific product by a user, or updates the existing review if one already exists.
     * If a review already exists for the given user and product, it will be updated with the new rating and comment.
     * If no existing review is found, a new review is created and added.
     *
     * @param userId The ID of the user adding the review.
     * @param productId The ID of the product being reviewed.
     * @param rate The rating given to the product by the user (usually between 1 and 5).
     * @param comment The comment provided by the user for the product.
     */
    suspend fun addReview(userId: Int, productId: Int, rate: Int, comment: String)

    /**
     * Updates the rating of a product.
     * This method will update the product's rating in the database.
     *
     * @param productId The ID of the product whose rating needs to be updated.
     * @param newRate The new rating for the product (usually between 1 and 5).
     */
    suspend fun updateProductRating(productId: Int, newRate: Int)
}