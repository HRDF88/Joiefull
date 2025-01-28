package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.data.webservice.GetProductApiResponse

/**
 * Interface defining the contract for a repository that fetches product data from a remote API.
 * Provides a method for retrieving product information from the API.
 */
interface GetProductRepositoryApiInterface {

    /**
     * Retrieves a list of products from the remote API.
     *
     * @return A list of [GetProductApiResponse] objects representing the products fetched from the API.
     * @throws Exception if the API request fails or an error occurs during the network call.
     */
    suspend fun getProduct(): List<GetProductApiResponse>
}