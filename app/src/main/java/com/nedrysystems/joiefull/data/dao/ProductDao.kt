package com.nedrysystems.joiefull.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedrysystems.joiefull.data.entity.ProductEntity
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo

@Dao
interface ProductDao {
    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getProductLocalInfo(id: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProductLocalInfo(product: ProductEntity)

    @Query("SELECT * FROM product")
    suspend fun getAllProductsLocalInfo(): List<ProductEntity>

    @Query("UPDATE product SET favorite = :favorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, favorite: Boolean)
}
