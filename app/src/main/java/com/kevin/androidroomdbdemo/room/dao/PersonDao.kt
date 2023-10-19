package com.kevin.androidroomdbdemo.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kevin.androidroomdbdemo.room.entity.Person


@Dao
interface PersonDao {


    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>
    @Insert
    suspend fun insert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Update
    suspend fun update(person: Person)


}