package com.nedrysystems.joiefull.ui.detail

import com.nedrysystems.joiefull.domain.model.User
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel

/**
 * Represents the UI state for the product details screen.
 *
 * @property products A list of products to be displayed. Defaults to an empty list.
 * @property isLoading Indicates whether the data is currently being loaded. Defaults to `false`.
 * @property error An optional error message resource ID if an error occurs. Defaults to `null`.
 * @property errorMessage An optional error message as a string. This can be used when the error is not linked to a string resource. Defaults to `null`.
 * @property user The currently authenticated user, if available. Defaults to `null`.
 */
data class DetailUiState(
    val products: List<ProductUiModel> = emptyList(),
    val isLoading: Boolean = false,
    var error: Int? = null,
    val errorMessage: String? = null,
    val user : User? = null
)
