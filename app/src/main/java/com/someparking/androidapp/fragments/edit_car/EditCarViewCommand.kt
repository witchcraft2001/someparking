package com.someparking.androidapp.fragments.edit_car

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class EditCarViewCommand : ViewCommand
object ShowNetworkErrorCommand : EditCarViewCommand()
object FinishCommand : EditCarViewCommand()
data class ShowTextErrorCommand(val message: String) : EditCarViewCommand()

