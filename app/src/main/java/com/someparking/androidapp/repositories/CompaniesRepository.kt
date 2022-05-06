package com.someparking.androidapp.repositories

import com.someparking.androidapp.domain.CompanyData
import com.someparking.androidapp.domain.RepositoryResult
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CompaniesRepository @Inject constructor() {
    fun getCompanies(): Single<RepositoryResult<List<CompanyData>>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(listOf(
                CompanyData(1, "Microsoft"),
                CompanyData(2, "Google"),
                CompanyData(3, "Apple"),
                CompanyData(4, "JetBrains"),
                CompanyData(5, "Yandex"),
            )) }

    }
}