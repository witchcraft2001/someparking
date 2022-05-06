package com.someparking.androidapp.domain.mappers

import com.someparking.androidapp.domain.ProfileData
import com.someparking.androidapp.domain.UserData

fun ProfileData.toUserData(): UserData {
    return UserData(userId, name, surname, midname)
}
