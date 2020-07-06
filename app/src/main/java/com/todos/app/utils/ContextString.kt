package com.todos.app.utils

import android.content.Context

sealed class ContextString {
    abstract fun format(context: Context): String
}

class TextString(val text: String) : ContextString() {
    override fun format(context: Context): String = text
}

class ResourceString(val id: Int) : ContextString() {
    override fun format(context: Context): String = context.getString(id)
}

class ResourceStringFormat(val id: Int, val values: Array<Any>) : ContextString() {
    override fun format(context: Context): String = context.getString(id, *values)
}

class ResourceQuantityStringFormat(val id: Int, val count: Int, val values: Array<Any>) : ContextString() {
    override fun format(context: Context): String = context.resources.getQuantityString(id, count, *values)
}