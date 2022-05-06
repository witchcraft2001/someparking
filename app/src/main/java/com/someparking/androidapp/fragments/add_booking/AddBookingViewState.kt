package com.someparking.androidapp.fragments.add_booking

import com.someparking.androidapp.core.base.mvvm.ViewState
import com.someparking.androidapp.domain.CarData
import com.someparking.androidapp.domain.CompanyData
import java.util.*

data class AddBookingViewState(
    val isProgressVisible: Boolean = false,
    val date: Calendar? = null,
    val start: Calendar? = null,
    val end: Calendar? = null,
    val place: String? = null,
    val isPlacesVisible: Boolean = true,
    val isCommentIncorrect: Boolean = false,
    val isDateIncorrect: Boolean = false,
    val isStartTimeIncorrect: Boolean = false,
    val isEndTimeIncorrect: Boolean = false,
    val company: String? = null,
    val cars: List<CarData> = listOf(),
    val companies: List<CompanyData> = listOf(),
    val freePlaces: List<String> = listOf(),
    val carId: Long? = null,
    val companyId: Long? = null,
    val comment: String? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val dateTime: Long? = null,
): ViewState
