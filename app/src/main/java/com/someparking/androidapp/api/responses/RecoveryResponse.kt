package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class RecoveryResponse(
    @SerializedName("id")
    val id: Long
)
