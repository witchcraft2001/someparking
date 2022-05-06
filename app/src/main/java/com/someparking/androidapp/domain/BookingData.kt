package com.someparking.androidapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class BookingData(
    val id: Long,
    val start: Date,
    val end: Date,
    val status: String,
    val carNum: String,
    val parkingSpace: String?
) :Parcelable
