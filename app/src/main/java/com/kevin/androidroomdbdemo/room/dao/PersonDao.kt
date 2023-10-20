package com.kevin.androidroomdbdemo.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kevin.androidroomdbdemo.room.entity.Person


@Dao
interface PersonDao {


    // Query to get all persons
    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>

    // Insert person to database
    @Insert
    suspend fun insert(person: Person): Long

    // Delete person from database
    @Delete
    suspend fun delete(person: Person): Int

    // Update person in database
    @Update
    suspend fun update(person: Person): Int


}