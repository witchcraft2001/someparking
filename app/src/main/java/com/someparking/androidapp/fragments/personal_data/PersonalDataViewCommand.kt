package com.someparking.androidapp.fragments.personal_data

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class PersonalDataViewCommand : ViewCommand
object ShowNetworkErrorCommand : PersonalDataViewCommand()
object ShowSuccessToastCommand : PersonalDataViewCommand()
data class ShowTextErrorCommand(val text: String) : PersonalDataViewCommand()
object FinishCommand : PersonalDataViewCommand()
