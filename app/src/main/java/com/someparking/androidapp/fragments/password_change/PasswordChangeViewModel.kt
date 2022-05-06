package com.someparking.androidapp.fragments.password_change

import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.repositories.ProfileRepository
import com.someparking.androidapp.services.UserService
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.properties.Delegates

class PasswordChangeViewModel @Inject constructor(
        private val userService: UserService,
        private val profileRepository: ProfileRepository
) : BaseViewModel<PasswordChangeViewState>() {

    private var state: PasswordChangeViewState by Delegates.observable(PasswordChangeViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    fun onBackClicked() {
        _viewCommands.onNext(FinishCommand)
    }

    fun onSendClicked() {
        checkPassword()
        if (state.firstPassword.isNullOrEmpty() || state.secondPassword.isNullOrEmpty() || state.isPasswordIncorrect) {
            return
        }
        with(state) {
            try {
                profileRepository.updatePassword(
                        userService.user.userId,
                        firstPassword ?: throw IllegalArgumentException("Не указан пароль"),
                )
                        .doOnSubscribe {
                            state = state.copy(isProgressVisible = true)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally { state = state.copy(isProgressVisible = false) }
                        .subscribe({ result ->
                            when (result) {
                                is RepositoryResult.Success -> {
                                    _viewCommands.onNext(ShowSuccessToastCommand)
                                    _viewCommands.onNext(FinishCommand)
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
                Timber.e(e)
                _viewCommands.onNext(ShowTextErrorCommand(e.message
                        ?: "Не предвиденная ошибка, пожалуйста, повторите запрос позже"))
            }
        }
    }

    private fun checkPassword() {
        state = state.copy(isPasswordIncorrect = (!state.firstPassword.isNullOrEmpty() ||
                !state.secondPassword.isNullOrEmpty()) &&
                state.firstPassword != state.secondPassword)
    }

    fun onFirstPasswordChanged(text: String) {
        if (text == state.firstPassword) {
            return
        }
        state = state.copy(firstPassword = text, isChanged = true)
        checkPassword()
    }

    fun onSecondPasswordChanged(text: String) {
        if (text == state.secondPassword) {
            return
        }
        state = state.copy(secondPassword = text, isChanged = true)
        checkPassword()
    }

    init {
        state = state.copy(isChanged = false)
    }
}