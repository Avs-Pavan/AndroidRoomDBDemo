package com.kevin.androidroomdbdemo

import android.view.View
import android.widget.EditText


fun EditText.textString(): String {
    return this.text.toString()
}

fun EditText.textInt(): Int {
    return this.text.toString().toInt()
}

fun EditText.textLong(): Long {
    return this.text.toString().toLong()
}

fun EditText.validate(): Boolean {
    return if (this.textString().isNotEmpty())
        true
    else {
        this.error = "Field cannot be empty"
        false
    }
}


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}