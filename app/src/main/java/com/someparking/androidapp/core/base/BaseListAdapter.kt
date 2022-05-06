package com.someparking.androidapp.core.base

interface BaseListAdapter<T> {
    fun add(newItem: T)
    fun add(newItems: List<T>?)
    fun addAtPosition(position: Int, newItem: T)
    fun remove(position: Int)
    fun clearAll()
}