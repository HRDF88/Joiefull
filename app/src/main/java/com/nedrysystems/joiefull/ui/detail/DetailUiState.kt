package com.nedrysystems.joiefull.ui.detail

import com.nedrysystems.joiefull.domain.model.User
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel

data class DetailUiState(
    val products: List<ProductUiModel> = emptyList(),
    val isLoading: Boolean = false,
    var error: String = "",
    val user : User? = null
)
