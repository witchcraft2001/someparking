package com.someparking.androidapp.domain.mappers

import com.someparking.androidapp.domain.BookingData
import com.someparking.androidapp.domain.models.BookingModel

fun BookingData.toBookingModel() : BookingModel =
    BookingModel(id, start, end, status, carNum, parkingSpace)

fun BookingModel.toBookingData() : BookingData =
    BookingData(id, start, end, status, carNum, parkingSpace)