package com.nedrysystems.joiefull.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedrysystems.joiefull.domain.model.ProductUiModel
import com.nedrysystems.joiefull.domain.usecase.product.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    // État de la liste des produits
    private val _products = MutableStateFlow<List<ProductUiModel>>(emptyList())
    open val products: StateFlow<List<ProductUiModel>> = _products.asStateFlow()

    // Gestion des erreurs ou du chargement
    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    open val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Récupération des produits
    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _products.value = getProductsUseCase.execute()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
