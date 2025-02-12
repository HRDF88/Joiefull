package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.repositoryInterface.GetProductLocalRepositoryInterface
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import javax.inject.Inject

/**
 * Repository class that interacts with the local database (via [ProductDao]) to fetch, insert, update, and manage product data.
 * It implements the [GetProductLocalRepositoryInterface] and provides methods to interact with local product information.
 *
 * @constructor Injects the [ProductDao] instance to perform database operations.
 *
 * @param productDao The DAO instance used to interact with the local product data.
 */
class GetProductLocalRepository @Inject constructor(
    private val productDao: ProductDao
) : GetProductLocalRepositoryInterface {

    /**
     * Retrieves all local product information from the database.
     * It maps the result of the DAO's query to a list of [ProductLocalInfo] objects.
     *
     * @return A list of [ProductLocalInfo] representing the products retrieved from the local database.
     */
    override suspend fun getLocalProductInfo(): List<ProductLocalInfo> {
        return productDao.getAllProductsLocalInfo()
            .map { ProductLocalInfo(0).fromDto(it) }
    }

    /**
     * Retrieves the local information of a product by its ID.
     * It fetches the [ProductEntity] from the database and converts it into a [ProductLocalInfo] object.
     *
     * @param id The unique identifier of the product.
     * @return A [ProductLocalInfo] object containing the product's local information, or null if not found.
     */
    override suspend fun getProductLocalInfo(id: Int): ProductLocalInfo? {
        // Fetches the ProductEntity from the local database
        val productEntity = productDao.getProductLocalInfo(id)

        // If productEntity is not null, it is converted to ProductLocalInfo
        return productEntity?.let {
            // Converts ProductEntity to ProductLocalInfo using a conversion function
            ProductLocalInfo(id = it.id, favorite = it.favorite, rate = it.rate)
        }
    }

    /**
     * Inserts or updates a product's local information in the database.
     * The [ProductLocalInfo] is converted to a [ProductEntity] before being passed to the DAO for insertion or update.
     *
     * @param productLocalInfo The product information to insert or update.
     */
    override suspend fun insertOrUpdateProductLocalInfo(productLocalInfo: ProductLocalInfo) {
        // Converts ProductLocalInfo to ProductEntity using the toDto() function
        val productEntity = productLocalInfo.toDto()

        // Calls the DAO to insert or update the product in the local database
        productDao.insertOrUpdateProductLocalInfo(productEntity)
    }

    /**
     * Updates the "favorite" status of a product in the local database.
     * This updates the value of the favorite field in the database for the product with the specified ID.
     *
     * @param id The unique identifier of the product.
     * @param favorite The new favorite status to set for the product.
     */
    override suspend fun updateFavoriteStatus(id: Int, favorite: Boolean) {
        productDao.updateFavoriteStatus(id, favorite)
    }
}


