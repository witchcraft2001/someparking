package com.someparking.androidapp.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneValidator @Inject constructor() {
    fun clearPhone(phone: String): String {
        var number = phone.replace("-", "")
            .replace("+", "")
            .replace(" ", "")
            .replace("(", "")
            .replace(")", "")
        if (number.startsWith('8')) {
            number = number.replaceFirst('8', '7')
        }
        return number
    }

    fun isCorrectPhone(phone: String): Boolean {
        return PHONE_REGEX.containsMatchIn(phone)
    }

    companion object {
        private val PHONE_REGEX = Regex("^(\\+7|7|8)\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    }
}