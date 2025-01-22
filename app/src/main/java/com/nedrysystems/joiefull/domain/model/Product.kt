package com.nedrysystems.joiefull.domain.model

/**
 * Data class representing a product with associated details.
 *
 * @property id The unique identifier of the product.
 * @property picture A [Picture] object containing the URL and description of the product image.
 * @property name The name of the product.
 * @property category The category to which the product belongs (e.g., accessories, clothing, etc.).
 * @property likes The number of likes the product has received.
 * @property price The current price of the product.
 * @property originalPrice The original price of the product before any discounts.
 */
data class Product(
    val id: Int,
    val picture: Picture,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    val originalPrice: Double
)
