package com.mpcs.memorizeme.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mpcs.memorizeme.Interface.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
