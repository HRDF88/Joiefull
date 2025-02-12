package com.nedrysystems.joiefull.repositoryTest

import com.nedrysystems.joiefull.data.dao.ReviewDao
import com.nedrysystems.joiefull.data.entity.ReviewEntity
import com.nedrysystems.joiefull.data.repository.ReviewRepository
import com.nedrysystems.joiefull.domain.model.Review
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReviewRepositoryTest {

    @Mock
    private lateinit var reviewDao: ReviewDao

    private lateinit var reviewRepository: ReviewRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        reviewRepository = ReviewRepository(reviewDao)
    }

    @Test
    fun `addReview should update existing review if it already exists`() = runBlocking {
        // Arrange
        val userId = 1
        val productId = 100
        val rate = 5
        val comment = "Great product!"

        // Mock an existing review
        val existingReview = ReviewEntity(id = 1, userId = userId, productId = productId, rate = 3, comment = "Old comment")
        Mockito.`when`(reviewDao.getReviewByUserAndProduct(userId, productId)).thenReturn(existingReview)

        // Act
        reviewRepository.addReview(userId, productId, rate, comment)

        // Assert: Verify that the reviewDao.insertReview was called with the updated review
        val updatedReview = existingReview.copy(rate = rate, comment = comment)
        Mockito.verify(reviewDao).insertReview(updatedReview)
    }

    @Test
    fun `addReview should insert new review if none exists`() = runBlocking {
        // Arrange
        val userId = 1
        val productId = 100
        val rate = 5
        val comment = "Great product!"

        // Mock no existing review
        Mockito.`when`(reviewDao.getReviewByUserAndProduct(userId, productId)).thenReturn(null)

        // Act
        reviewRepository.addReview(userId, productId, rate, comment)

        // Assert: Verify that the reviewDao.insertReview was called with the new review
        val newReview = Review(id = 0, userId = userId, productId = productId, rate = rate, comment = comment)
        Mockito.verify(reviewDao).insertReview(newReview.toDto())
    }

    @Test
    fun `updateProductRating should call DAO updateProductRating`() = runBlocking {
        // Arrange
        val productId = 100
        val newRate = 4

        // Act
        reviewRepository.updateProductRating(productId, newRate)

        // Assert: Verify that reviewDao.updateProductRating was called with the correct parameters
        Mockito.verify(reviewDao).updateProductRating(productId, newRate)
    }

}
