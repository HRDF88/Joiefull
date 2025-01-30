package com.nedrysystems.joiefull.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedrysystems.joiefull.data.webservice.PictureApiResponse
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel
import com.nedrysystems.joiefull.domain.usecase.productApi.GetProductUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.GetLocalProductInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.GetProductLocalInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.InsertOrUpdateProductLocalInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.UpdateFavoriteStatusUseCase
import com.nedrysystems.joiefull.ui.mapper.ProductUIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the state and business logic related to the home screen.
 * This class is responsible for fetching products from both the API and local storage,
 * merging the data, handling loading and error states, and updating the UI state.
 *
 * @property getProductUseCase The use case for fetching products from the API.
 * @property getLocalProductInfoUseCase The use case for fetching locally stored product information.
 * @property insertOrUpdateProductLocalInfoUseCase The use case for inserting or updating local product information.
 * @constructor Initializes the ViewModel with the required use cases.
 */
@HiltViewModel
open class HomeViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getLocalProductInfoUseCase: GetLocalProductInfoUseCase,
    private val insertOrUpdateProductLocalInfoUseCase: InsertOrUpdateProductLocalInfoUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val getProductLocalInfoUseCase: GetProductLocalInfoUseCase
) : ViewModel() {

    // Holds the current state of the UI, including products, loading status, and error messages.
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Fetches products when the ViewModel is initialized.
    init {
        fetchProducts()
    }

    /**
     * Updates the UI state to show the provided error message.
     *
     * @param errorMessage The error message to display.
     */
    private fun onError(errorMessage: String) {
        Log.e("AddCandidateViewModel", errorMessage)
        _uiState.update { currentState -> currentState.copy(error = errorMessage) }
    }

    /**
     * Resets the error state after the error has been processed or acknowledged.
     */
    fun updateErrorState() {
        _uiState.update { currentState -> currentState.copy(error = "") }
    }

    /**
     * Fetches products from both the API and local storage, merging them into a single list.
     * Handles loading and error states during the process.
     */
    private fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = "")
            try {
                val products = getProducts()
                _uiState.value = _uiState.value.copy(products = products, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur : ${e.message}"
                )
            }
        }
    }

    /**
     * Fetches the list of products from the API and local storage.
     * If a local product is missing, it will be created with default values.
     *
     * @return A list of [ProductUiModel] representing the merged products.
     */
    private suspend fun getProducts(): List<ProductUiModel> {
        mutableListOf<ProductUiModel>()
        val productUIMapper = ProductUIMapper()

        return try {
            // Fetch products from the API
            val apiProducts = getProductUseCase.invoke()
            Log.d(
                "HomeViewModel",
                "API Products: ${apiProducts.size}"
            ) // Log pour vérifier les produits API

            // Fetch local products
            val localProducts = getLocalProductInfoUseCase.execute()
            Log.d(
                "HomeViewModel",
                "Local Products: ${localProducts.size}"
            ) // Log pour vérifier les produits locaux
            val localProductsMap = localProducts.associateBy { it.id }


            // Merge API and local data
            apiProducts.map { apiProduct ->
                val localProduct = localProductsMap[apiProduct.id]

                // If the local product does not exist, insert a new one with default values
                if (localProduct == null) {
                    insertOrUpdateProductLocalInfoUseCase.execute(
                        ProductLocalInfo(
                            id = apiProduct.id,
                            favorite = false,
                            rate = null
                        )
                    )
                }

                // Map API and local data to a ProductUiModel
                productUIMapper.mapToUiModel(apiProduct, localProduct)

            }
        } catch (exception: Exception) {
            // In case of failure, update UI state with error
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Erreur lors de la récupération des produits : ${exception.message}"
            )

            val localProducts = getLocalProductInfoUseCase.execute()
            localProducts.map { localProduct ->
                // Map local products with default API values
                ProductUiModel(
                    id = localProduct.id,
                    picture = PictureApiResponse("", ""),
                    name = "Produit local inconnu", // Default name
                    category = "Inconnu", // Default category
                    likes = 0, // No information on likes
                    price = 0.0, // Default price
                    originalPrice = null, // No original price
                    favorite = localProduct.favorite,
                    rate = localProduct.rate
                )
            }
        }

    }

    /**
     * Toggles the favorite status of a product. This method will change the "favorite" status of the specified product
     * and update both the local UI state and the database with the new status. It also handles error cases by logging
     * the error and updating the UI state with an error message.
     *
     * The function performs the following steps:
     * 1. It toggles the favorite status of the given product.
     * 2. It logs the product's information before updating the status.
     * 3. It updates the favorite status of the product in the database using the [updateFavoriteStatusUseCase].
     * 4. After updating the database, it fetches the updated product information using the [getProductLocalInfoUseCase].
     * 5. It updates the UI state with the new list of products, reflecting the toggled favorite status.
     * 6. In case of an error, it logs the exception and updates the UI state with an error message.
     *
     * @param product The [ProductUiModel] whose favorite status is to be toggled.
     */
    fun toggleFavorite(product: ProductUiModel) {
        viewModelScope.launch {
            try {
                // Toggle the favorite status of the product
                val newFavoriteStatus = !product.favorite

                // Log product details before updating
                Log.d(
                    "HomeViewModel",
                    "Avant mise à jour: ${product.id} - Favori: ${product.favorite}"
                )

                // Update the favorite status in the database using the UseCase
                updateFavoriteStatusUseCase.execute(product.id, newFavoriteStatus)

                // Retrieve the updated product from local storage
                val updatedProduct = getProductLocalInfoUseCase.execute(product.id)
                Log.d("HomeViewModel", "Produit récupéré après mise à jour: $updatedProduct")

                // Update the local UI state with the new favorite status
                val updatedProducts = _uiState.value.products.map {
                    if (it.id == product.id) it.copy(favorite = newFavoriteStatus) else it
                }

                // Update the UI state with the modified product list
                _uiState.value = _uiState.value.copy(products = updatedProducts)

            } catch (e: Exception) {
                // Handle errors
                Log.e("HomeViewModel", "Erreur lors de la mise à jour du favori", e)
                _uiState.value = _uiState.value.copy(error = "Impossible de mettre en favori")
            }
        }
    }

}