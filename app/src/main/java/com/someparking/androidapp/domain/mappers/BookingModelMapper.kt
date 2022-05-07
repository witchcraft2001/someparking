package com.someparking.androidapp.domain.mappers

import com.someparking.androidapp.domain.BookingData
import com.someparking.androidapp.domain.models.BookingModel
import java.util.*

fun BookingData.toBookingModel(): BookingModel =
    BookingModel(
        id,
        Calendar.getInstance().apply { time = start },
        Calendar.getInstance().apply { time = end },
        status,
        carNum,
        parkingSpace)

fun BookingModel.toBookingData(): BookingData =
    BookingData(id, start.time, end.time, status, carNum, parkingSpace)