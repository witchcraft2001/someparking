package com.someparking.androidapp.fragments.show_booking

import com.someparking.androidapp.core.base.mvvm.ViewState
import com.someparking.androidapp.domain.BookingData
import java.util.*

data class ShowBookingViewState(
    val isProgressVisible: Boolean = false,
    val booking: BookingData? = null,
    val start: Calendar? = null,
    val end: Calendar? = null,
    val place: String? = null,
    val isPlacesVisible: Boolean = true,
    val isCancelButtonVisible: Boolean = true,
    val isActive: Boolean = false,
    val isEarly: Boolean = false,
    val timer: String? = null
) : ViewState
