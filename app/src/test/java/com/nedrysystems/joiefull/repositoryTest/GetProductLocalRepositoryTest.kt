package com.nedrysystems.joiefull.repositoryTest

import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.entity.ProductEntity
import com.nedrysystems.joiefull.data.repository.GetProductLocalRepository
import com.nedrysystems.joiefull.domain.model.ProductLocalInfo
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetProductLocalRepositoryTest {

    @Mock
    private lateinit var productDao: ProductDao

    private lateinit var getProductLocalRepository: GetProductLocalRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getProductLocalRepository = GetProductLocalRepository(productDao)
    }

    @Test
    fun `getLocalProductInfo should return list of ProductLocalInfo`() = runBlocking {
        // Arrange
        val productEntities = listOf(ProductEntity(id = 1, favorite = true, rate = 4))
        Mockito.`when`(productDao.getAllProductsLocalInfo()).thenReturn(productEntities)

        // Act
        val result = getProductLocalRepository.getLocalProductInfo()

        // Assert
        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals(true, result[0].favorite)
    }

    @Test
    fun `getProductLocalInfo should return ProductLocalInfo when product exists`() = runBlocking {
        // Arrange
        val productEntity = ProductEntity(id = 1, favorite = true, rate = 4)
        Mockito.`when`(productDao.getProductLocalInfo(1)).thenReturn(productEntity)

        // Act
        val result = getProductLocalRepository.getProductLocalInfo(1)

        // Assert
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals(true, result?.favorite)
    }

    @Test
    fun `getProductLocalInfo should return null when product does not exist`() = runBlocking {
        // Arrange
        Mockito.`when`(productDao.getProductLocalInfo(1)).thenReturn(null)

        // Act
        val result = getProductLocalRepository.getProductLocalInfo(1)

        // Assert
        assertNull(result)
    }

    @Test
    fun `insertOrUpdateProductLocalInfo should call DAO insertOrUpdate`() = runBlocking {
        // Arrange
        val productLocalInfo = ProductLocalInfo(id = 1, favorite = true, rate = 4)
        val productEntity = productLocalInfo.toDto()

        // Act
        getProductLocalRepository.insertOrUpdateProductLocalInfo(productLocalInfo)

        // Assert
        Mockito.verify(productDao).insertOrUpdateProductLocalInfo(productEntity)
    }

    @Test
    fun `updateFavoriteStatus should call DAO updateFavoriteStatus`() = runBlocking {
        // Arrange
        val productId = 1
        val favoriteStatus = true

        // Act
        getProductLocalRepository.updateFavoriteStatus(productId, favoriteStatus)

        // Assert
        Mockito.verify(productDao).updateFavoriteStatus(productId, favoriteStatus)
    }


}
