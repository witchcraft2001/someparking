package com.someparking.androidapp.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmailValidator @Inject constructor() {
    fun isCorrectEmail(phone: String): Boolean {
        return EMAIL_REGEX.containsMatchIn(phone)
    }

    companion object {
        val EMAIL_REGEX = Regex("^.+@.+\\..+$")
    }
}