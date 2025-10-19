package com.proyecto.braingasha.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.proyecto.braingasha.data.dao.CardDao
import com.proyecto.braingasha.data.dao.UserDao
import com.proyecto.braingasha.data.entity.Card
import com.proyecto.braingasha.data.entity.User
import com.proyecto.braingasha.data.entity.UserCard

@Database(
    entities = [User::class, Card::class, UserCard::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "braingasha_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
