package com.someparking.androidapp.repositories

import com.someparking.androidapp.common.Contants.MILLIS_IN_SEC
import com.someparking.androidapp.domain.BookingData
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.TransactionResult
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BookingsRepository @Inject constructor() {
    fun getBookings(userId: Long): Single<RepositoryResult<List<BookingData>>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map {
                val current = System.currentTimeMillis()
                RepositoryResult.Success(
                    listOf(
                        BookingData(
                            1,
                            Date(current),
                            Date(current + TimeUnit.HOURS.toMillis(1)),
                            "Active",
                            "A123AA123",
                            "B-123"
                        ),
                        BookingData(
                            2,
                            Date(current + TimeUnit.HOURS.toMillis(2)),
                            Date(current + TimeUnit.HOURS.toMillis(3)),
                            "Active",
                            "A123AA123",
                            "B-123"
                        ),
                        BookingData(
                            3,
                            Date(current + TimeUnit.HOURS.toSeconds(5)),
                            Date(current + TimeUnit.HOURS.toSeconds(6)),
                            "Active",
                            "A123AA123",
                            "B-123"
                        )
                    )
                )
            }
    }

    fun getFreeParkingSpaces(userId: Long): Single<RepositoryResult<List<String>>> {
        return Single.timer(100, TimeUnit.MILLISECONDS)
            .map {
                RepositoryResult.Success(
                    listOf(
                        "B-100",
                        "B-101",
                        "B-105",
                        "B-107",
                        "B-200",
                        "C-101",
                        "C-110",
                        "D-103",
                    )
                )
            }
    }

    fun addBooking(
        userId: Long,
        carId: Long,
        start: String,
        end: String,
        companyId: Long,
        parkingSpace: String,
        comment: String
    ): Single<RepositoryResult<TransactionResult>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(TransactionResult.SUCCESS) }
    }

    fun cancelBooking(
        userId: Long,
        bookingId: Long
    ): Single<RepositoryResult<TransactionResult>> {
        return Single.timer(1000, TimeUnit.MILLISECONDS)
            .map { RepositoryResult.Success(TransactionResult.SUCCESS) }
    }
}