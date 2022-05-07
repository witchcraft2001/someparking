package com.someparking.androidapp.domain.models

import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseListItem
import java.util.*

data class BookingModel(
    val id: Long,
    val start: Calendar,
    val end: Calendar,
    val status: String,
    val carNum: String,
    val parkingSpace: String?
) : BaseListItem {
    override fun getLayoutId(): Int = R.layout.layout_booking_list_item
}