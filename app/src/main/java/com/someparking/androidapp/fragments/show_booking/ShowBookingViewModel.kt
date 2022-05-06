package com.someparking.androidapp.fragments.show_booking

import com.github.terrakok.cicerone.Router
import com.someparking.androidapp.common.Contants.MILLIS_IN_SEC
import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.domain.BookingData
import com.someparking.androidapp.domain.BookingState
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.repositories.BookingsRepository
import com.someparking.androidapp.services.UserService
import com.someparking.androidapp.storage.preferences.Preferences
import com.someparking.androidapp.utils.TimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class ShowBookingViewModel @AssistedInject constructor(
    @Assisted
    private val data: BookingData,
    @Assisted
    private val router: Router,
    private val bookingsRepository: BookingsRepository,
    private val userService: UserService,
    private val timeUtils: TimeUtils,
    preferences: Preferences,
) : BaseViewModel<ShowBookingViewState>() {

    private var state: ShowBookingViewState by Delegates.observable(ShowBookingViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    private val serverDelta = preferences.getServerTimeDeltaSync()

    init {
        val now = (System.currentTimeMillis() - serverDelta) // MILLIS_IN_SEC
        val status = parseState(data.status)
        val inactiveStatus = status == BookingState.CANCELED ||
                status == BookingState.REJECTED ||
                status == BookingState.UNKNOWN
        state = state.copy(
            booking = data,
            start = Calendar.getInstance().apply { time = data.start },
            end = Calendar.getInstance().apply { time = data.end },
            isActive = now >= data.start.time && now <= data.end.time && !inactiveStatus,
            isEarly = now < data.start.time && !inactiveStatus,
            isCancelButtonVisible = !inactiveStatus
        )
        if (state.isActive || state.isEarly) {
            scheduleTimer()
        }
    }

    private fun parseState(state: String): BookingState {
        return when (state.lowercase()) {
            BookingState.APPROVED.value -> BookingState.APPROVED
            BookingState.PENDING.value -> BookingState.PENDING
            BookingState.CANCELED.value -> BookingState.CANCELED
            BookingState.COMPLETED.value -> BookingState.COMPLETED
            BookingState.REJECTED.value -> BookingState.REJECTED
            BookingState.CHECK_IN.value -> BookingState.CHECK_IN
            BookingState.CHECK_OUT.value -> BookingState.CHECK_OUT
            BookingState.CHECK_OUT_WAIT.value -> BookingState.CHECK_OUT_WAIT
            BookingState.UNDER_CONSIDERATION.value -> BookingState.UNDER_CONSIDERATION
            else -> BookingState.UNKNOWN
        }
    }

    private fun scheduleTimer() {
        Observable.interval(0L, 1L, TimeUnit.MINUTES)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                state.booking?.let {
                    val now = System.currentTimeMillis() - serverDelta
                    val elapse = if (state.isEarly) {
                        it.start.time
                    } else {
                        it.end.time
                    }
                    val time = elapse - now
                    state =
                        state.copy(timer = timeUtils.longMillisToTime(time, withSeconds = false))
                }
            }, { throwable ->
                Timber.e(throwable)
            })
            .safeSubscribe()
    }

    fun onBackClicked() {
        router.exit()
    }

    fun onCancelBookingClicked() {
        state.booking?.also {
            bookingsRepository
                .cancelBooking(
                    userService.user.userId,
                    it.id
                )
                .doOnSubscribe {
                    state = state.copy(isProgressVisible = true)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { state = state.copy(isProgressVisible = false) }
                .subscribe({ result ->
                    when (result) {
                        is RepositoryResult.Success -> {
                            router.exit()
                        }
                        is RepositoryResult.Error -> {
                            _viewCommands.onNext(
                                ShowTextErrorCommand(
                                    result.exception.message
                                        ?: "Не определенная ошибка, пожалуйста, повторите запрос позже"
                                )
                            )
                        }
                    }
                }, { throwable ->
                    Timber.e(throwable)
                    _viewCommands.onNext(ShowNetworkErrorCommand)
                })
                .safeSubscribe()
        }
    }

    @AssistedFactory
    interface Factory {
        fun get(data: BookingData, router: Router): ShowBookingViewModel
    }
}