package com.nedrysystems.joiefull.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the local database.
 * This entity corresponds to the "user" table, which stores the user's information such as their unique ID, name, and profile picture.
 *
 * @property id The unique identifier for the user. It is automatically generated by the database.
 * @property name The name of the user.
 * @property picture The URL or file path to the user's profile picture.
 */

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val picture: String
)