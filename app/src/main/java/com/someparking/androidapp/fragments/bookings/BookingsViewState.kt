package com.someparking.androidapp.fragments.bookings

import com.someparking.androidapp.core.base.mvvm.ViewState
import com.someparking.androidapp.domain.models.BookingModel

data class BookingsViewState(
    val items: List<BookingModel> = emptyList(),
    val isProgressVisible: Boolean = false,
): ViewState
