package com.nedrysystems.joiefull.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedrysystems.joiefull.data.entity.ReviewEntity

@Dao
interface ReviewDao {

    /**
     * Inserts a review for a product. If a review from the same user already exists for the product, it will be replaced.
     * @param review The review to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    /**
     * Updates the rating of a product.
     * @param productId The ID of the product to update the rating.
     * @param newRate The new rating value to be set for the product.
     */
    @Query("UPDATE product SET rate = :newRate WHERE id = :productId")
    suspend fun updateProductRating(productId: Int, newRate: Int)

    /**
     * Retrieves a review by the user for a specific product.
     * @param userId The ID of the user who gave the review.
     * @param productId The ID of the product for which the review is being retrieved.
     * @return The review entity if found, otherwise null.
     */
    @Query("SELECT * FROM review WHERE userId = :userId AND productId = :productId")
    suspend fun getReviewByUserAndProduct(userId: Int, productId: Int): ReviewEntity?
}