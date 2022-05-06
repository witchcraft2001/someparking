package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class ParkingSpaceListResponse(
    @SerializedName("list")
    val list: List<String>
)

