package com.nedrysystems.joiefull.domain.model

import com.nedrysystems.joiefull.data.entity.ReviewEntity

/**
 * Data class representing a review for a product made by a user.
 * This class encapsulates the details of a review, including the user who wrote it,
 * the product being reviewed, the rating given, and any associated comment.
 *
 * @property id The unique identifier of the review.
 * @property userId The ID of the user who created the review.
 * @property productId The ID of the product being reviewed.
 * @property rate The rating given to the product, typically on a scale from 1 to 5.
 * @property comment The textual comment provided by the user about the product.
 */
data class Review(
    val id: Int,
    val userId: Int,
    val productId: Int,
    val rate: Int,
    val comment: String
) {

    /**
     * Converts this [Review] instance to a [ReviewEntity] to be stored in the database.
     *
     * @return A [ReviewEntity] instance containing the same data as this [Review].
     */
    fun toDto(): ReviewEntity {
        return ReviewEntity(
            id = this.id,
            userId = this.userId,
            productId = this.productId,
            rate = this.rate,
            comment = this.comment
        )
    }

    /**
     * Creates a [Review] instance from a [ReviewEntity], typically used when converting data
     * retrieved from the database back into the domain model.
     *
     * @param reviewEntity The [ReviewEntity] instance to convert.
     * @return A [Review] instance containing the data from the given [ReviewEntity].
     */
    companion object {
        fun fromDto(reviewEntity: ReviewEntity): Review {
            return Review(
                id = reviewEntity.id,
                userId = reviewEntity.userId,
                productId = reviewEntity.productId,
                rate = reviewEntity.rate,
                comment = reviewEntity.comment
            )
        }
    }
}

