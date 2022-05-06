package com.someparking.androidapp.fragments.calls

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class CallsViewCommand : ViewCommand
class CallNumberViewCommand(val number: String) : CallsViewCommand()