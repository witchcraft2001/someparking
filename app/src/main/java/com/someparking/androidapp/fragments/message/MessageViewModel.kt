package com.someparking.androidapp.fragments.message

import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.core.base.mvvm.onNext
import com.someparking.androidapp.domain.RepositoryResult
import com.someparking.androidapp.repositories.FeedbackRepository
import com.someparking.androidapp.services.UserService
import com.someparking.androidapp.helpers.ProfileHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.properties.Delegates

class MessageViewModel @Inject constructor(
    private val feedbackRepository: FeedbackRepository,
    private val userService: UserService,
    private val profileHelper: ProfileHelper
) : BaseViewModel<MessageViewState>() {

    private var state: MessageViewState by Delegates.observable(MessageViewState()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            _viewState.onNext(newValue)
        }
    }

    private fun retrieveCompanies() {
        feedbackRepository.getCompanyList(userService.user.userId)
            .doOnSubscribe { state = state.copy(isProgressVisible = true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { state = state.copy(isProgressVisible = false) }
            .subscribe({ result ->
                when (result) {
                    is RepositoryResult.Success -> state =
                        state.copy(companies = result.data, isFormVisible = true)
                    is RepositoryResult.Error -> _viewCommands.onNext(ShowNetworkErrorCommand)
                }
            },
                { throwable ->
                    Timber.e(throwable)
                    _viewCommands.onNext(ShowNetworkErrorCommand)
                })
            .safeSubscribe()
    }

    fun onCompanySelected(position: Int) {
        state.companies?.get(position)?.let {
            state = state.copy(companyId = it.id)
        }
    }

    fun onBackClicked() {
        _viewCommands.onNext(FinishMessageCommand)
    }

    fun onSendClicked() {
        checkName()
        checkPhone()
        checkText()
        checkWorkplace()
        if (state.isWorkplaceIncorrect || state.isTextIncorrect ||
            state.isPhoneIncorrect || state.isNameIncorrect
        ) {
            return
        }
        with(state) {
            companies?.let { companies ->
                try {
                    feedbackRepository.sendFeedback(
                        userService.user.userId,
                        companyId ?: throw IllegalArgumentException("Не указана компания"),
                        workplace
                            ?: throw IllegalArgumentException("Не указано место проведения работ"),
                        text ?: throw IllegalArgumentException("Не указан текст заявки"),
                        profileHelper.getPhoneValid(phone),
                        name ?: throw IllegalArgumentException("Не указано ФИО")
                    )
                        .doOnSubscribe {
                            state = state.copy(isProgressVisible = true, isFormVisible = false)
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally { state = state.copy(isProgressVisible = false) }
                        .subscribe({ result ->
                            when (result) {
                                is RepositoryResult.Success ->
                                    state = state.copy(
                                        isMessageSentVisible = true,
                                        isFormVisible = false
                                    )
                                is RepositoryResult.Error -> {
                                    state = state.copy(isFormVisible = true)
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
                    _viewCommands.onNext(ShowTextErrorCommand(e.message ?: "Не ожиданная ошибка"))
                }
            }
        }
    }

    private fun checkText() {
        state = state.copy(isTextIncorrect = state.text.isNullOrBlank())
    }

    private fun checkWorkplace() {
        state = state.copy(isWorkplaceIncorrect = state.workplace.isNullOrBlank())
    }

    private fun checkName() {
        state = state.copy(isNameIncorrect = state.name.isNullOrBlank())
    }

    private fun checkPhone() {
        val phone = state.phone
        state = state.copy(
            isPhoneIncorrect = !(!phone.isNullOrBlank() && profileHelper.isPhoneValid(phone))
        )
    }

    fun onThanksClicked() {
        _viewCommands.onNext(FinishMessageCommand)
    }

    fun onNameChanged(text: String) {
        state = state.copy(name = text)
    }

    fun onPhoneChanged(text: String) {
        state = state.copy(phone = text)
    }

    fun onTextChanged(text: String) {
        state = state.copy(text = text)
    }

    fun onWorkplaceChanged(text: String) {
        state = state.copy(workplace = text)
    }

    init {
        state = state.copy(
                name = "${userService.user.surname} ${userService.user.name} ${userService.user.midname}",
        )
        retrieveCompanies()
    }
}