package com.someparking.androidapp.api.responses

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status")
    val status: ResponseStatus,
    @SerializedName("answer")
    val answer: String?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("error_code")
    val errorCode: Int?
)

enum class ResponseStatus {
    @SerializedName("success")
    SUCCESS,
    @SerializedName("error")
    ERROR
}
