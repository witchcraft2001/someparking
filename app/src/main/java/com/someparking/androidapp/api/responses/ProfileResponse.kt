package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("midname") val midname: String,
)
