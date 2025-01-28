package com.nedrysystems.joiefull.domain.model

import com.nedrysystems.joiefull.data.entity.ProductEntity

/**
 * Data class representing local information for a product such as its favorite status and rating.
 * This class is used to store additional information like whether a product is marked as a favorite
 * and the product's rating, which are not available from the API.
 *
 * @property id The unique identifier of the product, matching the product ID from the API.
 * @property favorite A boolean indicating whether the product is marked as a favorite. Defaults to false.
 * @property rate An optional integer representing the rating of the product. Can be null if no rating is provided.
 */
data class ProductLocalInfo(
    val id: Int,
    val favorite: Boolean = false,
    val rate: Double? = null
) {

    /**
     * Converts the current [ProductLocalInfo] instance into a [ProductEntity] for use with the local database.
     *
     * @return A [ProductEntity] object containing the same product information.
     */
    fun toDto(): ProductEntity {
        return ProductEntity(
            id = this.id,
            favorite = this.favorite,
            rate = this.rate

        )
    }

    /**
     * Creates a [ProductLocalInfo] instance from a given [ProductEntity].
     * This function is used to map a database entity into the local product representation.
     *
     * @param productEntity The [ProductEntity] instance to be converted.
     * @return A new [ProductLocalInfo] object containing the mapped product data.
     */
    fun fromDto(productEntity: ProductEntity): ProductLocalInfo {
        return ProductLocalInfo(
            id = productEntity.id,
            favorite = productEntity.favorite,
            rate = productEntity.rate
        )
    }
}