package com.someparking.androidapp.fragments.add_car

import com.github.terrakok.cicerone.Router
import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.core.utils.guard
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.fragments.message.ShowNetworkErrorCommand
import com.someparking.androidapp.repositories.CarsRepository
import com.someparking.androidapp.services.UserService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import kotlin.properties.Delegates

class AddCarViewModel @AssistedInject constructor(
    @Assisted
    private val router: Router,
    private val carsRepository: CarsRepository,
    private val userService: UserService,
) : BaseViewModel<AddCarViewState>() {

    private var state: AddCarViewState by Delegates.observable(AddCarViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    fun onBackClicked() {
        router.exit()
    }

    fun onAddCarClicked() {
        if (state.carnumber.isNullOrBlank()) {
            _viewCommands.onNext(ShowTextErrorCommand("Укажите гос номер автомобиля"))
            return
        }
        if (state.brand.isNullOrBlank()) {
            _viewCommands.onNext(ShowTextErrorCommand("Укажите марку автомобиля"))
            return
        }
        if (state.model.isNullOrBlank()) {
            _viewCommands.onNext(ShowTextErrorCommand("Укажите модель автомобиля"))
            return
        }
        if (state.comment.isNullOrBlank()) {
            _viewCommands.onNext(ShowTextErrorCommand("Укажите комментарий"))
            return
        }

        val (number, brand, model, comment) = guard(
            state.carnumber,
            state.brand,
            state.model,
            state.comment
        ) {
            Timber.e(Exception("Unspecified fields: number=${state.carnumber}, brand=${state.brand}, model=${state.model}, comment=${state.comment}"))

            return
        }
        carsRepository
            .addCar(
                userService.user.userId,
                number, brand, model, comment
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

    fun onCarnumberChanged(text: String) {
        state = state.copy(carnumber = text)
    }

    fun onBrandChanged(text: String) {
        state = state.copy(brand = text)
    }

    fun onModelChanged(text: String) {
        state = state.copy(model = text)
    }

    fun onCommentChanged(text: String) {
        state = state.copy(comment = text)
    }

    @AssistedFactory
    interface Factory {
        fun get(router: Router): AddCarViewModel
    }
}