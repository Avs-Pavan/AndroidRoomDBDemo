package com.kevin.androidroomdbdemo.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.androidroomdbdemo.room.dao.PersonDao
import com.kevin.androidroomdbdemo.room.entity.Person

@Database(entities = [Person::class], version = 1)
abstract class PersonDataBase() : RoomDatabase() {
    abstract fun personDao(): PersonDao
}