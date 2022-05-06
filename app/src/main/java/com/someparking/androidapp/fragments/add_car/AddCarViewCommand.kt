package com.someparking.androidapp.fragments.add_car

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class AddCarViewCommand : ViewCommand

object ShowNetworkErrorCommand : AddCarViewCommand()
data class ShowTextErrorCommand(val message: String) : AddCarViewCommand()