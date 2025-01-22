package com.nedrysystems.joiefull.domain.model

/**
 * Data class representing a picture associated with a product.
 *
 * @property url The URL of the image.
 * @property description A description of the image, providing additional context.
 */
data class Picture(
    val url: String,
    val description: String
)
