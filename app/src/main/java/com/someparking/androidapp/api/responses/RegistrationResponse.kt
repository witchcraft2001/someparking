package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("id")
    val id: Long
)
