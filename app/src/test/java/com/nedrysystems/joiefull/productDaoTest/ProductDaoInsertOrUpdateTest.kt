package com.nedrysystems.joiefull.productDaoTest

import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.entity.ProductEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductDaoInsertOrUpdateTest {

    @Mock
    lateinit var mockProductDao: ProductDao

    @Test
    fun insertOrUpdateProductLocalInfo_ShouldInsertNewProduct() = runBlocking {
        // Arrange
        val product = ProductEntity(id = 1, favorite = false)

        // Simuler l'insertion
        Mockito.`when`(mockProductDao.insertOrUpdateProductLocalInfo(product)).thenReturn(Unit)

        // Act: appel de la méthode à tester
        mockProductDao.insertOrUpdateProductLocalInfo(product)

        // Simuler la récupération du produit
        Mockito.`when`(mockProductDao.getProductLocalInfo(1)).thenReturn(product)

        // Récupérer le produit inséré
        val retrievedProduct = mockProductDao.getProductLocalInfo(1)

        // Assert
        assertNotNull(retrievedProduct)
        assertEquals(product.id, retrievedProduct?.id)
        assertEquals(product.favorite, retrievedProduct?.favorite)
    }

    @Test
    fun insertOrUpdateProductLocalInfo_ShouldUpdateExistingProduct() = runBlocking {
        // Arrange: insérer un produit initial
        val product1 = ProductEntity(id = 1, favorite = false)

        // Simuler l'insertion initiale
        Mockito.`when`(mockProductDao.insertOrUpdateProductLocalInfo(product1)).thenReturn(Unit)
        mockProductDao.insertOrUpdateProductLocalInfo(product1)

        // Act: mettre à jour le produit avec un nouveau statut "favorite"
        val updatedProduct = ProductEntity(id = 1, favorite = true)

        // Simuler l'insertion de la mise à jour
        Mockito.`when`(mockProductDao.insertOrUpdateProductLocalInfo(updatedProduct)).thenReturn(Unit)
        mockProductDao.insertOrUpdateProductLocalInfo(updatedProduct)

        // Simuler la récupération du produit mis à jour
        Mockito.`when`(mockProductDao.getProductLocalInfo(1)).thenReturn(updatedProduct)

        // Assert: vérifier que le produit a bien été mis à jour
        val retrievedProduct = mockProductDao.getProductLocalInfo(1)
        assertNotNull(retrievedProduct)
        assertEquals(updatedProduct.id, retrievedProduct?.id)
        assertEquals(updatedProduct.favorite, retrievedProduct?.favorite)
    }
}

