package com.nedrysystems.joiefull.ui.uiModel

import com.nedrysystems.joiefull.data.webservice.PictureApiResponse

/**
 * A data class representing a product as it will be displayed in the user interface.
 * This model combines both product data fetched from the API and additional local data such as
 * the favorite status and user rating.
 *
 * @property id The unique identifier of the product.
 * @property picture An object containing the picture URL and description of the product.
 * @property name The name of the product.
 * @property category The category to which the product belongs.
 * @property likes The number of likes the product has received.
 * @property price The current price of the product.
 * @property original_Price The original price of the product, which may be null if not available.
 * @property favorite A boolean indicating whether the product is marked as a favorite by the user. Defaults to false.
 * @property rate The rating of the product, which may be null if no rating is provided.
 */
data class ProductUiModel(
    val id: Int,
    val picture: PictureApiResponse,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    val original_Price: Double?,
    val favorite: Boolean = false,
    val rate: Double? = null
)
