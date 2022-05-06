package com.someparking.androidapp.domain

data class CompanyData(
    val id: Long,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
