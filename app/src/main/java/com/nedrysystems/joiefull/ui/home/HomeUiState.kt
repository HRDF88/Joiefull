package com.nedrysystems.joiefull.ui.home

import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel

/**
 * Represents the UI state for the home screen, containing information about the products and their loading status.
 * This class is used to manage and represent the UI state of the home screen in the application,
 * making it easier to handle loading states, errors, and the list of products to be displayed.
 *
 * @property products A list of [ProductUiModel] representing the products to be displayed in the UI. Defaults to an empty list.
 * @property isLoading A boolean indicating whether the data is currently being loaded. Defaults to false.
 * @property error A string containing an error message if an error occurs while fetching or displaying the data. Defaults to an empty string.
 */
data class HomeUiState(
    val products: List<ProductUiModel> = emptyList(),
    val isLoading: Boolean = false,
    var error: String = ""
)
