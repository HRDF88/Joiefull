package com.nedrysystems.joiefull.domain.model

import com.nedrysystems.joiefull.data.entity.UserEntity

/**
 * Data class representing a user in the system.
 * This class holds basic information about a user, including their unique identifier,
 * name, and picture.
 *
 * @property id The unique identifier of the user.
 * @property name The name of the user.
 * @property picture The URL or path to the user's picture.
 */
data class User(
    val id: Int = 0,
    val name: String,
    val picture: String
) {
    /**
     * Converts this [User] instance to a [UserEntity] to be stored in the database.
     *
     * @return A [UserEntity] instance containing the same data as this [User].
     */
    fun toDto(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            picture = this.picture

        )
    }

    /**
     * Creates a [User] instance from a [UserEntity], typically used when converting data
     * retrieved from the database back into the domain model.
     *
     * @param userEntity The [UserEntity] instance to convert.
     * @return A [User] instance containing the data from the given [UserEntity].
     */
    companion object {
        fun fromDto(userEntity: UserEntity): User {
            return User(
                id = userEntity.id,
                name = userEntity.name,
                picture = userEntity.picture
            )
        }
    }
}
