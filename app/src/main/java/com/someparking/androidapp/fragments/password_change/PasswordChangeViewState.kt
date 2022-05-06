package com.someparking.androidapp.fragments.password_change

import com.someparking.androidapp.core.base.mvvm.ViewState

data class PasswordChangeViewState(
        val isProgressVisible: Boolean = false,
        val isPasswordIncorrect: Boolean = false,
        val isChanged: Boolean = true,
        val firstPassword: String? = null,
        val secondPassword: String? = null,
): ViewState
