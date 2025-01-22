package com.nedrysystems.joiefull.domain.model

data class GetProductApiResponse(
    val id: Int,
    val picture: PictureApiResponse,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    val original_Price: Double?
)
data class PictureApiResponse(
    val url: String,
    val description: String
)