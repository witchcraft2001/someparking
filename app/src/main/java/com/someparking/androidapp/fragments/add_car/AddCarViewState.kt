package com.someparking.androidapp.fragments.add_car

import com.someparking.androidapp.core.base.mvvm.ViewState

data class AddCarViewState(
    val isProgressVisible: Boolean = false,
    val carnumber: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val comment: String? = null
): ViewState
