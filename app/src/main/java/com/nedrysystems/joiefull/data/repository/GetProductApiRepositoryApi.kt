package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.repositoryInterface.GetProductRepositoryApiInterface
import com.nedrysystems.joiefull.data.webservice.GetProductApiService
import com.nedrysystems.joiefull.data.webservice.GetProductApiResponse
import javax.inject.Inject

/**
 * Repository class that interacts with the remote API to fetch product data.
 * It implements the [GetProductRepositoryApiInterface] and uses [GetProductApiService] to retrieve product data from the API.
 *
 * @constructor Injects the [GetProductApiService] instance for making API calls.
 *
 * @param getProductApiService The service used to fetch product data from the API.
 */
class GetProductApiRepositoryApi @Inject constructor(private val getProductApiService: GetProductApiService) :
    GetProductRepositoryApiInterface {

    /**
     * Retrieves a list of products from the API.
     * This method calls the [getProductApiService.getProduct()] function to fetch data from the remote server.
     *
     * @return A list of [GetProductApiResponse] representing the products fetched from the API.
     * @throws Exception if the API request fails or an error occurs during the network call.
     */
    override suspend fun getProduct(): List<GetProductApiResponse> {
        return getProductApiService.getProduct()
    }
}