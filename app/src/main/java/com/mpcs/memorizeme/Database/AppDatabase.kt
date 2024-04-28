package com.mpcs.memorizeme.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mpcs.memorizeme.Interface.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE

//            check if there is any existing instance is present for our room database
//            if there exist an existing instance then we'll return that instance
            if (tempInstance != null) {
                return tempInstance
            }

//            If there is no any instance present for our database then we'll create a new instance
//            WHY SYNCHRONIZED ?? --> Because everything inside the synchronized block will be protected
//            by concurrent execution on multiple threads
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}