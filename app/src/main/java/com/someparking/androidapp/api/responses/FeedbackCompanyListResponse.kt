package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class FeedbackCompanyResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String
)
