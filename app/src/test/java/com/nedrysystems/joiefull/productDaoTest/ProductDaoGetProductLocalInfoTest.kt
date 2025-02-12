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
class ProductDaoGetProductLocalInfoTest {

    @Mock
    lateinit var mockProductDao: ProductDao  // Mock de l'interface ProductDao

    @Test
    fun getProductLocalInfo_ShouldReturnProductWhenExists() = runBlocking {
        // Arrange
        val product = ProductEntity(id = 1, favorite = false)
        Mockito.`when`(mockProductDao.getProductLocalInfo(1)).thenReturn(product)

        // Act
        val retrievedProduct = mockProductDao.getProductLocalInfo(1)

        // Assert
        assertNotNull(retrievedProduct)
        assertEquals(product.id, retrievedProduct?.id)
        assertEquals(product.favorite, retrievedProduct?.favorite)
    }
}
