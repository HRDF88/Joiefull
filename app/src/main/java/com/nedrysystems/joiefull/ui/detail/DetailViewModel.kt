package com.nedrysystems.joiefull.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nedrysystems.joiefull.data.webservice.PictureApiResponse
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import com.nedrysystems.joiefull.domain.model.User
import com.nedrysystems.joiefull.domain.usecase.productApi.GetProductUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.GetLocalProductInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.GetProductLocalInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.InsertOrUpdateProductLocalInfoUseCase
import com.nedrysystems.joiefull.domain.usecase.productLocal.UpdateFavoriteStatusUseCase
import com.nedrysystems.joiefull.domain.usecase.user.GetUserByIdUseCase
import com.nedrysystems.joiefull.ui.mapper.ProductUIMapper
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * DetailViewModel is responsible for managing the UI state and business logic
 * related to displaying product details. It interacts with use cases to fetch
 * product data from both remote and local sources and updates the UI accordingly.
 *
 * This ViewModel is annotated with `@HiltViewModel` for dependency injection using Hilt.
 *
 * @property getProductUseCase Use case to fetch product details from the API.
 * @property getLocalProductInfoUseCase Use case to retrieve local product information.
 * @property insertOrUpdateProductLocalInfoUseCase Use case to insert or update local product data.
 * @property updateFavoriteStatusUseCase Use case to update the favorite status of a product.
 * @property getProductLocalInfoUseCase Use case to get product details from the local database.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getLocalProductInfoUseCase: GetLocalProductInfoUseCase,
    private val insertOrUpdateProductLocalInfoUseCase: InsertOrUpdateProductLocalInfoUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val getProductLocalInfoUseCase: GetProductLocalInfoUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    /**
     * Holds the current selected product state.
     */
    private val _productState = MutableStateFlow<ProductUiModel?>(null)
    val productState: StateFlow<ProductUiModel?> = _productState.asStateFlow()

    /**
     * Holds the current UI state, including loading status, error messages, and product details.
     */
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _shareContent = MutableLiveData<String>()
    val shareContent: LiveData<String> = _shareContent

    /**
     * Fetches product details for a given product ID.
     *
     * This function updates the UI state to show a loading state, retrieves product details,
     * and updates the state accordingly. If the product is not found or an error occurs,
     * the state is updated with an appropriate error message.
     *
     * @param productId The unique identifier of the product to fetch.
     */
    fun fetchProductDetails(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = "")
            try {
                val products = getProducts().find { it.id == productId }
                if (products != null) {
                    _uiState.value =
                        _uiState.value.copy(products = listOf(products), isLoading = false)
                    _productState.value = products // Update the product state for display
                } else {
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, error = "Produit non trouvé")
                }
            } catch (e: Exception) {
                // Handle errors and update state
                Log.e("DetailViewModel", "Erreur lors de la récupération du produit", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Erreur lors de la récupération du produit"
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
        val productUIMapper = ProductUIMapper()

        return try {
            // Fetch products from the API
            val apiProducts = getProductUseCase.invoke()
            Log.d("DetailViewModel", "API Products: ${apiProducts.size}")

            // Fetch local products
            val localProducts = getLocalProductInfoUseCase.execute()
            Log.d("DetailViewModel", "Local Products: ${localProducts.size}")
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

            // Fallback to local products
            val localProducts = getLocalProductInfoUseCase.execute()
            return localProducts.map { localProduct ->
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
     * Toggles the favorite status of a given product.
     *
     * This function updates both local storage and the UI state to reflect
     * the new favorite status. It also updates the product's like count accordingly.
     *
     * @param product The [ProductUiModel] representing the product whose favorite status needs to be toggled.
     */
    fun toggleFavorite(product: ProductUiModel) {
        viewModelScope.launch {
            try {
                // Toggle the favorite status of the product
                val newFavoriteStatus = !product.favorite

                // Log product details before updating
                Log.d(
                    "DetailViewModel",
                    "Avant mise à jour: ${product.id} - Favori: ${product.favorite}"
                )

                // Update the favorite status in the database using the UseCase
                updateFavoriteStatusUseCase.execute(product.id, newFavoriteStatus)

                // Retrieve the updated product from local storage
                val updatedProduct = getProductLocalInfoUseCase.execute(product.id)
                Log.d("DetailViewModel", "Produit récupéré après mise à jour: $updatedProduct")

                // Update the product state with the new favorite status
                _productState.value = _productState.value?.copy(
                    favorite = newFavoriteStatus,
                    likes = if (newFavoriteStatus) _productState.value?.likes?.plus(1) ?: 0
                    else _productState.value?.likes?.minus(1) ?: 0
                )

                // Update the UI state product list
                val updatedProducts = _uiState.value.products.map {
                    if (it.id == product.id) it.copy(
                        favorite = newFavoriteStatus,
                        likes = if (newFavoriteStatus) it.likes + 1 else it.likes - 1
                    ) else it
                }

                // Update the UI state with the modified product list
                _uiState.value = _uiState.value.copy(products = updatedProducts)

            } catch (e: Exception) {
                // Handle errors
                Log.e("DetailViewModel", "Erreur lors de la mise à jour du favori", e)
                _uiState.value = _uiState.value.copy(error = "Impossible de mettre en favori")
            }
        }
    }

    /**
     * Prepares the content to be shared for a product.
     * This function constructs a shareable text containing the product's name.
     *
     * @param product The [ProductUiModel] containing the product details, particularly the name.
     * @property _shareContent A mutable state that holds the prepared shareable content.
     */
    fun prepareShareContent(product: ProductUiModel) {
        val text = "Découvrez ce produit : ${product.name}"
        _shareContent.value = text
    }

    /**
     * Fetches the user with ID 1 and updates the UI state with the user data.
     * If the user is found, the UI state will be updated with the user and loading status set to false.
     * If the user is not found, the UI state will be updated with a default user and an error message.
     */
    fun getUserAndUpdateState() {
        viewModelScope.launch {
            // Catch user with Id 1
            val user = getUserByIdUseCase.execute(1)

            if (user != null) {
                // if user exist in database cath user in UIState user
                _uiState.value = DetailUiState(
                    user = user,
                    isLoading = false
                )
            } else {
                // if user don't exist, handle error
                _uiState.value = DetailUiState(
                    user = User(id = 0, name = "Unknown", picture = ""),
                    isLoading = false,
                    error = "User not found"
                )
            }
        }
    }
}

