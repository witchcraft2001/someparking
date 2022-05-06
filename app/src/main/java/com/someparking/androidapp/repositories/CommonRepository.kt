package com.someparking.androidapp.repositories

import com.someparking.androidapp.domain.CommonData
import com.someparking.androidapp.domain.RepositoryResult
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CommonRepository @Inject constructor() {
    fun getCommonData(userId: Long): Single<RepositoryResult<CommonData>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map {
                RepositoryResult.Success(
                    CommonData(
                        "+380501111111",
                        "+380502222222",
                        "https://google.com"
                    )
                )
            }
    }
}