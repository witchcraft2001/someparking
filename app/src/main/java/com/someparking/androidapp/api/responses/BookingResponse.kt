package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class BookingResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("start")
    val start: Long,
    @SerializedName("end")
    val end: Long,
    @SerializedName("status")
    val status: String,
    @SerializedName("car_num")
    val carNum: String,
    @SerializedName("parking_space")
    val parkingSpace: String?
)