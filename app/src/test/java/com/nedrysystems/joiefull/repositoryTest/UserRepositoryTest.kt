package com.nedrysystems.joiefull.repositoryTest

import com.nedrysystems.joiefull.data.dao.UserDao
import com.nedrysystems.joiefull.data.entity.UserEntity
import com.nedrysystems.joiefull.data.repository.UserRepository
import com.nedrysystems.joiefull.domain.model.User
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
class UserRepositoryTest {

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRepository = UserRepository(userDao)
    }

    @Test
    fun `getUserById should return user when user exists`() = runBlocking {
        // Arrange
        val userId = 1
        val userEntity = UserEntity(
            id = userId,
            name = "John Doe",
            picture = "http://www.johnDoe.com/profilePicture.com"
        )
        val expectedUser = User(
            id = userId,
            name = "John Doe",
            picture = "http://www.johnDoe.com/profilePicture.com"
        )

        // Mock the DAO to return the UserEntity
        Mockito.`when`(userDao.getUserById(userId)).thenReturn(userEntity)

        // Act
        val result = userRepository.getUserById(userId)

        // Assert
        assertNotNull(result)
        assertEquals(expectedUser, result)
    }

    @Test
    fun `getUserById should return null when user does not exist`() = runBlocking {
        // Arrange
        val userId = 1

        // Mock the DAO to return null when no user is found
        Mockito.`when`(userDao.getUserById(userId)).thenReturn(null)

        // Act
        val result = userRepository.getUserById(userId)

        // Assert
        assertNull(result)
    }

}
