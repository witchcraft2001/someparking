package com.someparking.androidapp.fragments.home

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class HomeViewCommand : ViewCommand
object OpenCallsDialogViewCommand : HomeViewCommand()
object OpenMessageViewCommand : HomeViewCommand()
data class OpenUrlViewCommand(val url: String) : HomeViewCommand()