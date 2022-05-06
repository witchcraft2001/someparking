package com.someparking.androidapp.fragments.personal_data

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

class PersonalDataViewModel @Inject constructor(
        private val userService: UserService,
        private val profileRepository: ProfileRepository
) : BaseViewModel<PersonalDataViewState>() {

    private var state: PersonalDataViewState by Delegates.observable(PersonalDataViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    fun onBackClicked() {
        _viewCommands.onNext(FinishCommand)
    }

    fun onSendClicked() {
        checkName()
        checkSurname()
        checkMidname()
        if (state.isNameIncorrect || state.isSurnameIncorrect || state.isMidnameIncorrect) {
            return
        }
        with(state) {
            try {
                profileRepository.updateProfile(
                        userService.user.userId,
                        name ?: throw IllegalArgumentException("Не указано имя"),
                        surname ?: throw IllegalArgumentException("Не указана фамилия"),
                        midname ?: throw IllegalArgumentException("Не указано отчество")
                )
                        .doOnSubscribe {
                            state = state.copy(isProgressVisible = true)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally { state = state.copy(isProgressVisible = false) }
                        .subscribe({ result ->
                            when (result) {
                                is RepositoryResult.Success -> {
                                    userService.updatePersonalData(surname, name, midname)
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

    private fun checkName() {
        state = state.copy(isNameIncorrect = state.name.isNullOrBlank())
    }

    private fun checkSurname() {
        state = state.copy(isSurnameIncorrect = state.surname.isNullOrBlank())
    }

    private fun checkMidname() {
        state = state.copy(isMidnameIncorrect = state.midname.isNullOrBlank())
    }

    fun onNameChanged(text: String) {
        if (text == state.name) {
            return
        }
        state = state.copy(name = text, isChanged = true)
        checkName()
    }

    fun onMidnameChanged(text: String) {
        if (text == state.midname) {
            return
        }
        state = state.copy(midname = text, isChanged = true)
        checkMidname()
    }

    fun onSurnameChanged(text: String) {
        if (text == state.surname) {
            return
        }
        state = state.copy(surname = text, isChanged = true)
        checkSurname()
    }

    init {
        state = state.copy(
                name = userService.user.name,
                surname = userService.user.surname,
                midname = userService.user.midname,
        )
    }
}