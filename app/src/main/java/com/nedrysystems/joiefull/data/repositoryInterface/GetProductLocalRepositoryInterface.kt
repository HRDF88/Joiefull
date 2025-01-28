package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.domain.model.ProductLocalInfo

/**
 * Interface defining the contract for a local repository that manages product data.
 * Provides methods for retrieving, inserting, updating, and managing local product information.
 */
interface GetProductLocalRepositoryInterface {

    /**
     * Retrieves all local product information from the database.
     *
     * @return A list of [ProductLocalInfo] representing all products stored locally.
     */
    suspend fun getLocalProductInfo(): List<ProductLocalInfo>

    /**
     * Retrieves the local information of a product by its ID.
     *
     * @param id The unique identifier of the product.
     * @return A [ProductLocalInfo] object containing the product's local information, or null if the product does not exist in the local database.
     */
    suspend fun getProductLocalInfo(id: Int): ProductLocalInfo?

    /**
     * Inserts or updates a product's local information in the database.
     *
     * @param productLocalInfo The [ProductLocalInfo] object to insert or update.
     */
    suspend fun insertOrUpdateProductLocalInfo(productLocalInfo: ProductLocalInfo)

    /**
     * Updates the "favorite" status of a product in the local database.
     *
     * @param id The unique identifier of the product.
     * @param favorite The new favorite status to set for the product (true or false).
     */
    suspend fun updateFavoriteStatus(id: Int, favorite: Boolean)
}