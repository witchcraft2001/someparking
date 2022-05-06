package com.someparking.androidapp.fragments.home

import com.someparking.androidapp.core.base.mvvm.ViewState

data class HomeViewState(
    val isBackButtonVisible: Boolean = false
): ViewState
