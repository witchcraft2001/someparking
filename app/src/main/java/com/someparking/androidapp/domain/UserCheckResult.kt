package com.someparking.androidapp.domain

sealed class UserCheckResult {
    class Registered(val user: UserData) : UserCheckResult()
    object NotRegistered: UserCheckResult()
}
