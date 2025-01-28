package com.nedrysystems.joiefull.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.entity.ProductEntity

/**
 * Represents the Room database for the application.
 * The database contains a single table, `product`, and provides access to product-related data through the `ProductDao`.
 *
 * @constructor Creates the database instance with the specified version and entities.
 */
@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for performing operations on the `product` table.
     *
     * @return The [ProductDao] instance for querying and modifying product data.
     */
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Creates and returns a singleton instance of the [AppDatabase] if it hasn't been created yet.
         * If an instance already exists, it returns the existing one.
         *
         * @param context The context used to initialize the Room database.
         * @return The singleton instance of [AppDatabase].
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
