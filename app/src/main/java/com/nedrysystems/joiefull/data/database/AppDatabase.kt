package com.nedrysystems.joiefull.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nedrysystems.joiefull.data.dao.ProductDao
import com.nedrysystems.joiefull.data.dao.ReviewDao
import com.nedrysystems.joiefull.data.dao.UserDao
import com.nedrysystems.joiefull.data.entity.ProductEntity
import com.nedrysystems.joiefull.data.entity.ReviewEntity
import com.nedrysystems.joiefull.data.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Represents the Room database for the application.
 * The database contains a single table, `product`, and provides access to product-related data through the `ProductDao`.
 *
 * @constructor Creates the database instance with the specified version and entities.
 */
@Database(
    entities = [ProductEntity::class, UserEntity::class, ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for performing operations on the `product` table.
     *
     * @return The [ProductDao] instance for querying and modifying product data.
     */
    abstract fun productDao(): ProductDao

    /**
     * Provides access to the DAO for performing operations on the `user` table.
     *
     * @return The [UserDao] instance for querying and modifying user data.
     */
    abstract fun userDao(): UserDao

    /**
     * Provides access to the DAO for performing operations on the `review` table.
     *
     * @return The [ReviewDao] instance for querying and modifying review data.
     */
    abstract fun reviewDao(): ReviewDao

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

                )
                    // Ajoute un callback pour insérer un utilisateur avec un nom et une image par défaut
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Utiliser getDatabase pour récupérer l'instance de la base
                            val appDatabase = getDatabase(context)
                            GlobalScope.launch(Dispatchers.IO) {
                                // Insérer un utilisateur avec une image par défaut
                                appDatabase.userDao().insert(
                                    UserEntity(
                                        name = "Jocelyn Testing",
                                        picture = "https://static.wikia.nocookie.net/espritscriminels/images/c/c5/Reid_S9.jpg/revision/latest/scale-to-width-down/250?cb=20140830121341&path-prefix=fr"
                                    )
                                )
                            }
                        }
                    })

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
