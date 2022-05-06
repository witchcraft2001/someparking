package com.someparking.androidapp.core.extensions

import android.view.View

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

private var lastClickTimestamp = 0L

fun Any.setThrottledListener(delay: Long = 500L, actionListener: (Any) -> Unit) {
    val currentTimestamp = System.currentTimeMillis()
    val delta = currentTimestamp - lastClickTimestamp
    // 0L for hidden bug: launch app -> move system time back -> return to app ...
    if (delta !in 0L..delay) {
        lastClickTimestamp = currentTimestamp
        actionListener.invoke(this)
    }
}

@JvmOverloads
fun View.setThrottledClickListener(delay: Long = 500L, clickListener: (View) -> Unit) {
    setOnClickListener { view ->
        setThrottledListener(delay) { clickListener.invoke(view) }
    }
}