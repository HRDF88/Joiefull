package com.nedrysystems.joiefull.ui.mapper

import com.nedrysystems.joiefull.data.webservice.GetProductApiResponse
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import com.nedrysystems.joiefull.ui.uiModel.ProductUiModel

/**
 * A mapper class responsible for converting data from API responses and local data models to UI models.
 * This class ensures that the data is correctly mapped to a format suitable for display in the UI,
 * combining both API response data and locally stored information for a comprehensive product model.
 */
class ProductUIMapper {

    /**
     * Maps a product from the API response and local information to a UI model.
     * This method combines data from the API and local storage (if available) to create a [ProductUiModel]
     * that can be used to display the product in the user interface.
     *
     * @param apiResponse The response object containing product data from the API.
     * @param localInfo The local product information, which may include favorite status and rating.
     *                 This can be null if there is no local data available for the product.
     *
     * @return A [ProductUiModel] that represents the product's data, combining both API and local information.
     */
    fun mapToUiModel(
        apiResponse: GetProductApiResponse,
        localInfo: ProductLocalInfo?
    ): ProductUiModel {
        return ProductUiModel(
            id = apiResponse.id,
            picture = apiResponse.picture,
            name = apiResponse.name,
            category = apiResponse.category,
            likes = apiResponse.likes,
            price = apiResponse.price,
            original_Price = apiResponse.original_Price,
            favorite = localInfo?.favorite ?: false, // Default value is false if localInfo is null
            rate = localInfo?.rate // This can be null if no local rating is provided
        )
    }
}
