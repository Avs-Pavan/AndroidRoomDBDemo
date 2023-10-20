package com.kevin.androidroomdbdemo.extensions

import android.view.View
import android.widget.EditText


// Extension function to convert EditText to String
fun EditText.textString(): String {
    return this.text.toString()
}

// Extension function to convert EditText to Int
fun EditText.textInt(): Int {
    return this.text.toString().toInt()
}

// Extension function to convert EditText to Long
fun EditText.textLong(): Long {
    return this.text.toString().toLong()
}

// Extension function to validate EditText and show error if empty
fun EditText.validate(): Boolean {
    return if (this.textString().isNotEmpty())
        true
    else {
        this.error = "Field cannot be empty"
        false
    }
}

// Extension function to show view
fun View.show() {
    this.visibility = View.VISIBLE
}

// Extension function to hide view
fun View.hide() {
    this.visibility = View.GONE
}