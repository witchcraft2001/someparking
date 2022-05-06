package com.someparking.androidapp.domain

sealed class Message(val message: String) {
    class Toast(message: String) : Message(message)
    class Dialog(message: String, val title: String? = null): Message(message)
}