package com.nedrysystems.joiefull.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedrysystems.joiefull.R
import com.nedrysystems.joiefull.data.webservice.PictureApiResponse
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import com.nedrysystems.joiefull.domain.usecase.productApi.GetProductUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.GetLocalProductInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.GetProductLocalInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.InsertOrUpdateProductLocalInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.UpdateFavoriteStatusUseCase
import com.nedrysystems.joiefull.ui.mapper.ProductUIMapper
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel
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
    private val getProductLocalInfoUseCase: GetProductLocalInfoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Holds the current state of the UI, including products, loading status, and error messages.
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Fetches products when the ViewModel is initialized.
    init {
        fetchProducts()

        // Listen if a product has been updated from DetailScreen
        savedStateHandle.getLiveData<Int>("updatedProductId").observeForever { productId ->
            if (productId != null) {
                updateProductState(productId)
            }
        }
    }

    private val _productState = MutableStateFlow<ProductUiModel?>(null)
    val productState: StateFlow<ProductUiModel?> = _productState.asStateFlow()

    /**
     * Fetches products from both the API and local storage, merging them into a single list.
     * Handles loading and error states during the process.
     */
    private fun fetchProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val products = getProducts()
                _uiState.value = _uiState.value.copy(products = products, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "${e.message}"
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


            // Fetch local products
            val localProducts = getLocalProductInfoUseCase.execute()

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
                error = R.string.error_retrieving_products,
                errorMessage = "${exception.message}"
            )

            val localProducts = getLocalProductInfoUseCase.execute()
            localProducts.map { localProduct ->
                // Map local products with default API values
                ProductUiModel(
                    id = localProduct.id,
                    picture = PictureApiResponse("", ""),
                    name = "", // Default name
                    category = "", // Default category
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
     * @param product The [ProductUiModel] whose favorite status is to be toggled.
     */
    fun toggleFavorite(product: ProductUiModel) {
        viewModelScope.launch {
            try {
                // Toggle the favorite status of the product
                val newFavoriteStatus = !product.favorite

                // Update the favorite status in the database using the UseCase
                updateFavoriteStatusUseCase.execute(product.id, newFavoriteStatus)

                // Update _productState to also include likes
                _productState.value = _productState.value?.copy(favorite = newFavoriteStatus)

                //Updating _uiState.products
                val updatedProducts = _uiState.value.products.map {
                    if (it.id == product.id) it.copy(favorite = newFavoriteStatus) else it
                }

                // Update the UI state with the modified product list
                _uiState.value = _uiState.value.copy(products = updatedProducts)

            } catch (e: Exception) {
                // Handle errors
                _uiState.value = _uiState.value.copy(error = R.string.unable_to_favorite)
            }
        }
    }

    /**
     * Updates the state of a product after an update has occurred in another screen (such as DetailScreen).
     * This function retrieves the updated product from local storage and reflects the changes in the UI state.
     *
     * @param productId The ID of the product to update in the UI state.
     */
    private fun updateProductState(productId: Int) {
        viewModelScope.launch {
            try {
                val updatedProduct = getProductLocalInfoUseCase.execute(productId)

                // Update_uiState.products`
                _uiState.update { state ->
                    val updatedProducts = state.products.map {
                        if (it.id == productId) updatedProduct?.let { it1 ->
                            it.copy(
                                favorite = it1.favorite,
                            )
                        } else it
                    }.filterNotNull()
                    state.copy(products = updatedProducts)
                }
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(error = R.string.unable_to_update_product)
            }
        }
    }

    /**
     * Refreshes the list of products by re-fetching them.
     */
    fun refreshProducts() {
        viewModelScope.launch {
            fetchProducts()  // Call the function to retrieve the products again
        }
    }
}







