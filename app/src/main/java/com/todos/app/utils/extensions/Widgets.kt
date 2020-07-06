package com.todos.app.utils.extensions

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.gone() {
    this.visibility = View.GONE
}

fun ProgressBar.visible() {
    this.visibility = View.VISIBLE
}