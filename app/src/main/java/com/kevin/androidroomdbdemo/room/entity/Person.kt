package com.kevin.androidroomdbdemo.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Person Entity
 **/
@Entity
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo var age: Int?,
    @ColumnInfo var name: String?,
    @ColumnInfo var profession: String?,
    @ColumnInfo(name = "address", defaultValue = "Default address") var address: String?
)