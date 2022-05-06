package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)
