package com.someparking.androidapp.repositories

import com.someparking.androidapp.domain.FeedbackCompanyData
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.TransactionResult
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FeedbackRepository @Inject constructor() {
    fun getCompanyList(userId: Long): Single<RepositoryResult<List<FeedbackCompanyData>>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(
                listOf(
                    FeedbackCompanyData(1, "Apple"),
                    FeedbackCompanyData(2, "Microsoft"),
                    FeedbackCompanyData(3, "Google"),
                    FeedbackCompanyData(4, "Yandex"),
                )
            ) }
    }

    fun sendFeedback(
        userId: Long,
        companyId: Long,
        workplace: String,
        text: String,
        phone: String,
        name: String
    ): Single<RepositoryResult<TransactionResult>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(TransactionResult.SUCCESS) }
    }
}