package com.someparking.androidapp.fragments.personal_data

import com.someparking.androidapp.core.base.mvvm.ViewState

data class PersonalDataViewState(
    val isProgressVisible: Boolean = false,
    val isNameIncorrect: Boolean = false,
    val isSurnameIncorrect: Boolean = false,
    val isMidnameIncorrect: Boolean = false,
    val isChanged: Boolean = false,
    val surname: String? = null,
    val name: String? = null,
    val midname: String? = null,
): ViewState
