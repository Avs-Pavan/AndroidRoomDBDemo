package com.kevin.androidroomdbdemo.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.androidroomdbdemo.room.dao.PersonDao
import com.kevin.androidroomdbdemo.room.entity.Person


@Database(entities = [Person::class], version = 2)
abstract class PersonDataBase() : RoomDatabase() {
    // Abstract method to get PersonDao
    abstract fun personDao(): PersonDao
}