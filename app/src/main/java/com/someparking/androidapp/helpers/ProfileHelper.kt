package com.someparking.androidapp.helpers

import com.someparking.androidapp.domain.UsernameValidationResult
import com.someparking.androidapp.utils.EmailValidator
import com.someparking.androidapp.utils.PhoneValidator
import javax.inject.Inject

class ProfileHelper @Inject constructor(
    private val phoneValidator: PhoneValidator,
    private val emailValidator: EmailValidator
) {
    fun isUsernameValid(username: String?): UsernameValidationResult {
        return when {
            username == null -> UsernameValidationResult.NotValid
            phoneValidator.isCorrectPhone(username) ->
                UsernameValidationResult.ValidPhone(phoneValidator.clearPhone(username))
            emailValidator.isCorrectEmail(username) -> UsernameValidationResult.ValidEmail(username)
            else -> UsernameValidationResult.NotValid
        }
    }

    fun isPhoneValid(phone: String) = phoneValidator.isCorrectPhone(phone)

    fun isEmailValid(email: String) = emailValidator.isCorrectEmail(email)

    fun getPhoneValid(phone: String?) : String {
        if (!phone.isNullOrEmpty() && phoneValidator.isCorrectPhone(phone)) {
            return phoneValidator.clearPhone(phone)
        }
        throw IllegalArgumentException("$phone is not valid phone number")
    }
}
