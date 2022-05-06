package com.someparking.androidapp.fragments.profile

import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.storage.preferences.Preferences
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val preferences: Preferences
) : BaseViewModel<ProfileViewState>() {
    fun onPersonalDataClicked() {
        _viewCommands.onNext(OpenPersonalDataViewCommand)
    }

    fun onLogoutClicked() {
        preferences.clearOnLogout()
        _viewCommands.onNext(RestartApplicationViewCommand)
    }

    fun onContactDataClicked() {
        _viewCommands.onNext(OpenContactDataViewCommand)
    }

    fun onPasswordClicked() {
        _viewCommands.onNext(OpenPasswordChangeViewCommand)
    }

}