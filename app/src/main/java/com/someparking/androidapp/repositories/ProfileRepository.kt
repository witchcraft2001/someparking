package com.someparking.androidapp.repositories

import com.someparking.androidapp.domain.CheckCodeResult
import com.someparking.androidapp.domain.ProfileData
import com.someparking.androidapp.domain.RepositoryResult
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileRepository @Inject constructor() {

    fun getProfile(userId: Long): Single<RepositoryResult<ProfileData>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(
                ProfileData(1, "Dmitry", "Mikhaltchenkov", "Alexandrovich")
            ) }
    }

    fun updatePassword(
        userId: Long,
        password: String,
    ): Single<RepositoryResult<CheckCodeResult>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(CheckCodeResult.SUCCESS) }
    }

    fun updateProfile(
        userId: Long,
        name: String,
        surname: String,
        midname: String
    ): Single<RepositoryResult<CheckCodeResult>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(CheckCodeResult.SUCCESS) }
    }
}