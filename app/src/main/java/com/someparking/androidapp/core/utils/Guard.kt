package com.someparking.androidapp.core.utils

inline fun <T : Any> guard(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}
