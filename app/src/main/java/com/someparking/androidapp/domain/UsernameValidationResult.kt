package com.someparking.androidapp.domain

sealed class UsernameValidationResult {
    class ValidPhone(val phone: String) : UsernameValidationResult()
    class ValidEmail(val email: String) : UsernameValidationResult()
    object NotValid : UsernameValidationResult()
}
