package com.someparking.androidapp.fragments.cars

import com.github.terrakok.cicerone.Router
import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.domain.mappers.toCarData
import com.someparking.androidapp.domain.mappers.toCarModel
import com.someparking.androidapp.domain.models.CarModel
import com.someparking.androidapp.navigation.Screens
import com.someparking.androidapp.repositories.CarsRepository
import com.someparking.androidapp.services.UserService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import kotlin.properties.Delegates

class CarsViewModel @AssistedInject constructor(
    @Assisted
    private val router: Router,
    private val carsRepository: CarsRepository,
    private val userService: UserService,
) : BaseViewModel<CarsViewState>() {

    private var state: CarsViewState by Delegates.observable(CarsViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    private fun retrieveCars() {
        carsRepository.getCars(userService.user.userId)
            .doOnSubscribe { state = state.copy(isProgressVisible = true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { state = state.copy(isProgressVisible = false) }
            .subscribe(
                { result ->
                    when (result) {
                        is RepositoryResult.Success -> state =
                            state.copy(cars = result.data.map { it.toCarModel() })
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

    fun onAddCarClicked() {
        router.navigateTo(Screens.AddCarScreen)
    }

    fun onCarListChanged() {
        retrieveCars()
    }

    fun onClickItem(item: CarModel) {
        _viewCommands.onNext(ShowEditCarDialogCommand(item.toCarData()))
    }

    fun updateCars() {
        retrieveCars()
    }

    init {
        retrieveCars()
    }

    @AssistedFactory
    interface Factory {
        fun get(router: Router): CarsViewModel
    }
}