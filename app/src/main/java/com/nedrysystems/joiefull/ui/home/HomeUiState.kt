package com.nedrysystems.joiefull.ui.home

import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel

/**
 * Represents the UI state for the home screen.
 *
 * @property products A list of products to be displayed on the home screen. Defaults to an empty list.
 * @property isLoading Indicates whether the data is currently being loaded. Defaults to `false`.
 * @property error An optional error message resource ID if an error occurs. Defaults to `null`.
 * @property errorMessage An optional error message as a string, used when the error is not linked to a string resource. Defaults to `null`.
 */
data class HomeUiState(
    val products: List<ProductUiModel> = emptyList(),
    val isLoading: Boolean = false,
    var error: Int? = null,
    val errorMessage: String? = null,
)
