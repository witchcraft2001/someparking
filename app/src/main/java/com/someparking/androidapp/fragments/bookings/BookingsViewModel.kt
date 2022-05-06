package com.someparking.androidapp.fragments.bookings

import com.github.terrakok.cicerone.Router
import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.mappers.toBookingData
import com.someparking.androidapp.domain.mappers.toBookingModel
import com.someparking.androidapp.domain.mappers.toCarModel
import com.someparking.androidapp.domain.models.BookingModel
import com.someparking.androidapp.fragments.cars.CarsViewState
import com.someparking.androidapp.fragments.cars.ShowNetworkErrorCommand
import com.someparking.androidapp.fragments.cars.ShowTextErrorCommand
import com.someparking.androidapp.navigation.Screens
import com.someparking.androidapp.repositories.BookingsRepository
import com.someparking.androidapp.services.UserService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import kotlin.properties.Delegates

class BookingsViewModel @AssistedInject constructor(
    @Assisted
    private val router: Router,
    private val bookingsRepository: BookingsRepository,
    private val userService: UserService,
) : BaseViewModel<BookingsViewState>() {

    private var state: BookingsViewState by Delegates.observable(BookingsViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    fun onBookingClicked() {
        router.navigateTo(Screens.AddBookingScreen)
    }

    fun onItemClicked(item: BookingModel) {
        router.navigateTo(Screens.ShowBookingScreen(item.toBookingData()))
    }

    fun updateBookingList() {
        bookingsRepository.getBookings(userService.user.userId)
            .doOnSubscribe { state = state.copy(isProgressVisible = true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { state = state.copy(isProgressVisible = false) }
            .subscribe(
                { result ->
                    when (result) {
                        is RepositoryResult.Success -> state =
                            state.copy(items = result.data.map { it.toBookingModel() })
                        is RepositoryResult.Error -> _viewCommands.onNext(
                            ShowTextErrorCommand(
                                result.exception.message
                                    ?: "Произошла непредвиденная ошибка"
                            )
                        )
                    }
                },
                { throwable ->
                    Timber.e(throwable)
                    _viewCommands.onNext(ShowNetworkErrorCommand)
                })
            .safeSubscribe()
    }

    @AssistedFactory
    interface Factory {
        fun get(router: Router): BookingsViewModel
    }
}