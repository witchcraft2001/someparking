package com.someparking.androidapp.fragments.add_booking

import com.someparking.androidapp.core.base.mvvm.ViewCommand

sealed class AddBookingViewCommand : ViewCommand

object ShowNetworkErrorCommand : AddBookingViewCommand()
data class ShowTextErrorCommand(val message: String) : AddBookingViewCommand()