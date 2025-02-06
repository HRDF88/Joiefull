package com.nedrysystems.joiefull.data.repository

import com.nedrysystems.joiefull.data.dao.UserDao
import com.nedrysystems.joiefull.data.repositoryInterface.UserRepositoryInterface
import com.nedrysystems.joiefull.domain.model.User
import javax.inject.Inject

/**
 * Repository class responsible for handling the business logic related to user data.
 * It interacts with the data layer (DAO) to retrieve user information from the database.
 * This class provides a method to fetch a user by their ID.
 *
 * @constructor Creates an instance of the UserRepository. It injects the [UserDao] to perform database operations.
 *
 * @property userDao The DAO (Data Access Object) for managing user data in the database.
 */
class UserRepository @Inject constructor(private val userDao: UserDao) : UserRepositoryInterface {


    /**
     * Retrieves a user from the database by their ID.
     * If the user exists, it maps the [UserEntity] to a [User] object before returning it.
     * If the user does not exist, it returns null.
     *
     * @param userId The ID of the user to retrieve.
     * @return A [User] object corresponding to the provided userId, or null if the user does not exist.
     */
    override suspend fun getUserById(userId: Int): User? {
        // Fetch the user from the database by ID
        val userEntity = userDao.getUserById(userId)
        // Map the UserEntity to a User object if found, or return null
        return userEntity?.let {
            User.fromDto(it)
        }
    }

}

