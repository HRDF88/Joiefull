package com.nedrysystems.joiefull.domain.usecase.user

import com.nedrysystems.joiefull.data.repositoryInterface.UserRepositoryInterface
import com.nedrysystems.joiefull.domain.model.User
import javax.inject.Inject

/**
 * Use case for fetching a user by their ID.
 * This class encapsulates the logic to retrieve a user from the repository.
 *
 * @property userRepositoryInterface The repository interface for interacting with user data.
 */
class GetUserByIdUseCase @Inject constructor(private val userRepositoryInterface: UserRepositoryInterface) {

    /**
     * Executes the use case to retrieve a user by their ID.
     *
     * This method calls the repository to get the user associated with the provided user ID.
     *
     * @param userId The unique identifier of the user to be fetched.
     * @return A [User] object containing the user's data, or null if the user is not found.
     */
    suspend fun execute(userId: Int): User? {
        return userRepositoryInterface.getUserById(userId)
    }
}
