package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class CarResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("number")
    val number: String,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("status_name")
    val statusName: String?
)

data class CarListResponse(
    @SerializedName("list")
    val list: List<CarResponse>,
)