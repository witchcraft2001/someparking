package com.someparking.androidapp.fragments.show_booking

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class ShowBookingViewCommand : ViewCommand

object ShowNetworkErrorCommand : ShowBookingViewCommand()
data class ShowTextErrorCommand(val message: String) : ShowBookingViewCommand()