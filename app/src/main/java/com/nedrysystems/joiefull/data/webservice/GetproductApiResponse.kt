package com.nedrysystems.joiefull.data.webservice
import com.squareup.moshi.Json

/**
 * Represents the response from the API for a product.
 *
 * @property id The unique identifier of the product.
 * @property picture The [PictureApiResponse] object containing details about the product's picture.
 * @property name The name of the product.
 * @property category The category to which the product belongs.
 * @property likes The number of likes the product has received.
 * @property price The current price of the product.
 * @property originalPrice The original price of the product, if available (nullable).
 */
data class GetProductApiResponse(
    val id: Int,
    val picture: PictureApiResponse,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    @Json(name = "original_price")
    val originalPrice: Double?
)

/**
 * Represents the picture details of a product as part of the API response.
 *
 * @property url The URL of the product's picture.
 * @property description A textual description of the picture.
 */
data class PictureApiResponse(
    val url: String,
    val description: String
)