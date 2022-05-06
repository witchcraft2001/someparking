package com.someparking.androidapp.fragments.profile

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class ProfileViewCommand : ViewCommand

object OpenPersonalDataViewCommand : ProfileViewCommand()
object OpenContactDataViewCommand : ProfileViewCommand()
object OpenPasswordChangeViewCommand : ProfileViewCommand()
object RestartApplicationViewCommand : ProfileViewCommand()
