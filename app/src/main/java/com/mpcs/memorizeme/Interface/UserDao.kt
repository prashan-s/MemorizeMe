package com.mpcs.memorizeme.Interface

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mpcs.memorizeme.Database.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): User?

    @Query("SELECT MAX(highScore) FROM users")
    suspend fun getMaxHighScore(): Int?
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<User>?

}
