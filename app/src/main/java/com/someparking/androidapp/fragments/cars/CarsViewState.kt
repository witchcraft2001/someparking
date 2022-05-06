package com.someparking.androidapp.fragments.cars

import com.someparking.androidapp.core.base.mvvm.ViewState
import com.someparking.androidapp.domain.models.CarModel

data class CarsViewState(
    val cars: List<CarModel> = emptyList(),
    val isProgressVisible: Boolean = false,
): ViewState
