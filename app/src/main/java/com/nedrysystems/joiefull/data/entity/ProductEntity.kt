package com.nedrysystems.joiefull.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a product entity for Room database storage.
 * This class is used to persist product-related data, including its favorite status
 * and user rating, in the local database.
 *
 * @property id The unique identifier of the product, corresponding to the product ID.
 * @property favorite A boolean indicating whether the product is marked as a favorite by the user. Defaults to false.
 * @property rate An optional integer representing the rating of the product, can be null if not rated by the user.
 */
@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val favorite: Boolean = false,
    val rate: Double? = null

)

