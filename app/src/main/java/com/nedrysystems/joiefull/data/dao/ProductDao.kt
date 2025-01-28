package com.nedrysystems.joiefull.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedrysystems.joiefull.data.entity.ProductEntity

/**
 * Data Access Object (DAO) interface for interacting with the "product" table in the local database.
 * Provides methods to query, insert, update, and manipulate product data.
 */
@Dao
interface ProductDao {

    /**
     * Retrieves a product's local information by its ID.
     *
     * @param id The unique identifier of the product.
     * @return A [ProductEntity] containing the product's local information, or null if not found.
     */
    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getProductLocalInfo(id: Int): ProductEntity?


    /**
     * Inserts a new product or updates an existing product's information in the local database.
     * If a product with the same ID already exists, it will be replaced with the new data.
     *
     * @param product The product entity to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProductLocalInfo(product: ProductEntity)

    /**
     * Retrieves all products' local information from the "product" table.
     *
     * @return A list of [ProductEntity] representing all products in the database.
     */
    @Query("SELECT * FROM product")
    suspend fun getAllProductsLocalInfo(): List<ProductEntity>

    /**
     * Updates the "favorite" status of a product in the local database.
     *
     * @param id The unique identifier of the product.
     * @param favorite The new favorite status to be set for the product (true or false).
     */
    @Query("UPDATE product SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, favorite: Boolean)
}
