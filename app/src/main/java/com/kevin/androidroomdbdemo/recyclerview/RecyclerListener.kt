package com.kevin.androidroomdbdemo.recyclerview

interface RecyclerListener {
    fun onLongPress(item: Int)
    fun onClick(personId: Int, position: Int)
}