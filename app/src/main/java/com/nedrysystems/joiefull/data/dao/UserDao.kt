package com.nedrysystems.joiefull.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nedrysystems.joiefull.data.entity.UserEntity

@Dao
interface UserDao {

    /**
     * Inserts a user into the database. If a user with the same ID already exists,
     * the existing user record will be replaced.
     *
     * @param user The user entity to be inserted into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    /**
     * Retrieves a user from the database by their unique user ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user entity if found, otherwise returns null.
     */
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

}
