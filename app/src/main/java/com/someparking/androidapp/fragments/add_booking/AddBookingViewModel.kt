package com.someparking.androidapp.fragments.add_booking

import com.github.terrakok.cicerone.Router
import com.someparking.androidapp.common.Contants.MILLIS_IN_SEC
import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.core.utils.guard
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.UserRole
import com.someparking.androidapp.repositories.BookingsRepository
import com.someparking.androidapp.repositories.CarsRepository
import com.someparking.androidapp.repositories.CompaniesRepository
import com.someparking.androidapp.services.UserService
import com.someparking.androidapp.utils.TimeUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.*
import kotlin.properties.Delegates

class AddBookingViewModel @AssistedInject constructor(
    @Assisted
    private val router: Router,
    private val bookingsRepository: BookingsRepository,
    private val userService: UserService,
    private val companiesRepository: CompaniesRepository,
    private val carsRepository: CarsRepository,
    private val timeUtils: TimeUtils,
) : BaseViewModel<AddBookingViewState>() {

    private var state: AddBookingViewState by Delegates.observable(AddBookingViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    fun onBackClicked() {
        router.exit()
    }

    private fun retrieveData() {
        retrieveCompanies()
            .andThen(retrieveCars())
            .andThen(retrieveFreePlaces())
            .doOnSubscribe {
                state = state.copy(isProgressVisible = true)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { state = state.copy(isProgressVisible = false) }
            .subscribe({

            }, { throwable ->
                Timber.e(throwable)
                _viewCommands.onNext(ShowNetworkErrorCommand)
            })
            .safeSubscribe()
    }

    private fun retrieveCompanies(): Completable {
        return companiesRepository.getCompanies()
            .onErrorReturn { throwable -> RepositoryResult.Error(throwable) }
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable { result ->
                when (result) {
                    is RepositoryResult.Success -> {
                        state = state.copy(companies = result.data)
                        Completable.complete()
                    }
                    is RepositoryResult.Error -> Completable.error(result.exception)
                }
            }
    }

    private fun retrieveCars(): Completable {
        return carsRepository
            .getCars(userService.user.userId)
            .onErrorReturn { throwable -> RepositoryResult.Error(throwable) }
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable { result ->
                when (result) {
                    is RepositoryResult.Success -> {
                        state = state.copy(cars = result.data)
                        Completable.complete()
                    }
                    is RepositoryResult.Error -> Completable.error(result.exception)
                }
            }
    }

    private fun retrieveFreePlaces(): Completable {
//        return if (userService.user.roleId != UserRole.WORKER.value) {
//            state = state.copy(isPlacesVisible = false)
//            Completable.complete()
//        } else {
            return bookingsRepository
                .getFreeParkingSpaces(userService.user.userId)
                .onErrorReturn { throwable -> RepositoryResult.Error(throwable) }
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable { result ->
                    when (result) {
                        is RepositoryResult.Success -> {
                            state = state.copy(freePlaces = result.data)
                            Completable.complete()
                        }
                        is RepositoryResult.Error -> Completable.error(result.exception)
                    }
                }
//        }
    }

    private fun checkComment() {
        state = state.copy(isCommentIncorrect = state.comment.isNullOrBlank())
    }

    private fun checkDate() {
        state = state.copy(isDateIncorrect = state.date == null)
    }

    private fun checkStartTime() {
        state = state.copy(isStartTimeIncorrect = state.start == null)
    }

    private fun checkEndTime() {
        state = state.copy(isEndTimeIncorrect = state.end == null)
    }

    fun onAddBookingClicked() {
        checkComment()
        checkDate()
        checkStartTime()
        checkEndTime()
        if (state.isCommentIncorrect || state.isDateIncorrect || state.isStartTimeIncorrect || state.isEndTimeIncorrect) {
            return
        }
        val (date, start, end) = guard(state.date, state.start, state.end) {
            Timber.e(Exception("Unspecified date fields"))
            return
        }
        val startDate = date.clone() as Calendar
        val endDate = date.clone() as Calendar

        startDate.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY))
        startDate.set(Calendar.MINUTE, start.get(Calendar.MINUTE))
        startDate.set(Calendar.SECOND, 0)

        endDate.set(Calendar.HOUR_OF_DAY, end.get(Calendar.HOUR_OF_DAY))
        endDate.set(Calendar.MINUTE, end.get(Calendar.MINUTE))
        endDate.set(Calendar.SECOND, 0)

        if (startDate.timeInMillis <= System.currentTimeMillis()) {
            state = state.copy(isStartTimeIncorrect = state.start == null)
            _viewCommands.onNext(ShowTextErrorCommand("Время начала парковки не может быть меньше текущего времени"))
            return
        }
        if (endDate.timeInMillis <= startDate.timeInMillis) {
            state = state.copy(isEndTimeIncorrect = state.start == null)
            _viewCommands.onNext(ShowTextErrorCommand("Время окончания парковки не может быть меньше времени начала"))
            return
        }

        val startText = timeUtils.millisToString(startDate.timeInMillis)
        val endText = timeUtils.millisToString(endDate.timeInMillis)

        try {
            bookingsRepository
                .addBooking(
                    userService.user.userId,
                    state.carId ?: throw IllegalArgumentException("Не указан автомобиль"),
                    startText,
                    endText,
                    state.companyId ?: throw IllegalArgumentException("Не указана компания"),
                    state.place ?: "",
                    state.comment ?: throw IllegalArgumentException("Не указан комментарий"),
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
        } catch (e: Exception) {
            ShowTextErrorCommand(
                e.message ?: "Не определенная ошибка, пожалуйста, повторите запрос позже"
            )
        }
    }

    fun onCommentChanged(text: String) {
        state = state.copy(comment = text)
    }

    fun onCompanySelected(position: Int) {
        state = state.copy(companyId = state.companies[position].id)
    }

    fun onCarSelected(position: Int) {
        state = state.copy(carId = state.cars[position].id)
    }

    fun onPlaceSelected(position: Int) {
        state = state.copy(place = state.freePlaces[position])
    }

    fun onChangeDate(date: Calendar) {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (today.timeInMillis > date.timeInMillis) {
            _viewCommands.onNext(ShowTextErrorCommand("Дата начала парковки не может быть меньше текущей даты"))
            return
        }
        state = state.copy(date = date, dateTime = date.timeInMillis)
    }

    fun onChangeTimeStart(startTime: Calendar) {
        if (state.endTime != null && (startTime.timeInMillis + 60 * MILLIS_IN_SEC) >= state.endTime!!) {
            _viewCommands.onNext(ShowTextErrorCommand("Время начала парковки не может быть больше времени окончания"))
            return
        }
        state = state.copy(start = startTime, startTime = startTime.timeInMillis)
    }

    fun onChangeTimeEnd(endTime: Calendar) {
        if (state.startTime != null && (state.startTime!! + 60 * MILLIS_IN_SEC) >= endTime.timeInMillis) {
            _viewCommands.onNext(ShowTextErrorCommand("Время окончания парковки не может быть меньше времени начала"))
            return
        }
        state = state.copy(end = endTime, endTime = endTime.timeInMillis)
    }

    init {
        retrieveData()
    }

    @AssistedFactory
    interface Factory {
        fun get(router: Router): AddBookingViewModel
    }
}