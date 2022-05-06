package com.someparking.androidapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarData(
    val id: Long,
    val number: String,
    val brand: String,
    val model: String,
    val comment: String,
    val status: String?,
) : Parcelable {
    override fun toString(): String {
        return number
    }
}