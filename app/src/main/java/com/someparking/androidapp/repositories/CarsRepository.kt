package com.someparking.androidapp.repositories

import com.someparking.androidapp.domain.CarData
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.TransactionResult
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CarsRepository @Inject constructor() {
    fun getCars(userId: Long): Single<RepositoryResult<List<CarData>>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(listOf(
                CarData(1, "A123AA123", "BMW", "X5", "some comment", "active"),
                CarData(2, "A234AA123", "VW", "Polo", "some comment", "active"),
                CarData(3, "A345AA123", "VW", "Passat", "some comment", "active"),
            )) }
    }

    fun addCar(
        userId: Long, number: String, brand: String, model: String, comment: String
    ): Single<RepositoryResult<TransactionResult>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(TransactionResult.SUCCESS) }
    }

    fun updateCar(
        userId: Long, carId: Long, number: String, brand: String, model: String, comment: String
    ): Single<RepositoryResult<TransactionResult>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(TransactionResult.SUCCESS) }
    }

    fun removeCar(userId: Long, carId: Long, ): Single<RepositoryResult<TransactionResult>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(TransactionResult.SUCCESS) }
    }
}