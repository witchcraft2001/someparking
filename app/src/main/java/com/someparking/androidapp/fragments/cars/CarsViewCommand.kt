package com.someparking.androidapp.fragments.cars

import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.domain.CarData

sealed class CarsViewCommand : ViewCommand

object ShowNetworkErrorCommand : CarsViewCommand()
data class ShowTextErrorCommand(val message: String) : CarsViewCommand()
data class ShowEditCarDialogCommand(val item: CarData) : CarsViewCommand()