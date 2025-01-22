package com.nedrysystems.joiefull.domain.model

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
