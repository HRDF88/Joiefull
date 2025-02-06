package com.nedrysystems.joiefull.data.repositoryInterface

import com.nedrysystems.joiefull.domain.model.User

/**
 * Interface for the User Repository.
 * This interface defines the methods required for interacting with user data.
 * It provides methods for retrieving user details based on their ID.
 *
 * @see UserRepository The implementation of this interface.
 */
interface UserRepositoryInterface {

    /**
     * Retrieves a user by their unique ID.
     * This method fetches the user details associated with the provided user ID from the database.
     *
     * @param userId The ID of the user to retrieve.
     * @return A [User] object if the user exists, or `null` if no user is found with the given ID.
     */
    suspend fun getUserById(userId: Int): User?
}
