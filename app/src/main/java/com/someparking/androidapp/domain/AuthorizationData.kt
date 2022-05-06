package com.someparking.androidapp.domain

data class AuthorizationData(
    val userId: Long,
    val roleId: Long,
    val token: String
)