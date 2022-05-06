package com.someparking.androidapp.domain.models

import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseListItem

data class CarModel(
    val id: Long,
    val number: String,
    val brand: String,
    val model: String,
    val comment: String,
    val status: String?,
) : BaseListItem {
    override fun getLayoutId(): Int = R.layout.layout_car_list_item
}