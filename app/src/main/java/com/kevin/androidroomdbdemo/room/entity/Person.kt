package com.kevin.androidroomdbdemo.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val age: Int?,
    @ColumnInfo val name: String?,
    @ColumnInfo val profession: String?,
    @ColumnInfo(name = "address", defaultValue = "Default address") val address: String?
)