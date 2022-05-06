package com.someparking.androidapp.domain

data class FeedbackCompanyData(
    val id: Long,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}