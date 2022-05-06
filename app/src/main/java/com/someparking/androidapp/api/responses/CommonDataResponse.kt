package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName
import java.util.*

data class CommonDataResponse(
    @SerializedName("security_phone")
    val securityPhone: String,
    @SerializedName("parking_phone")
    val parkingPhone: String,
    @SerializedName("food_delivery_link")
    val foodDeliveryLink: String,
)
