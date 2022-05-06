package com.someparking.androidapp.fragments.calls

import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.services.CommonDataService
import javax.inject.Inject

class CallsViewModel @Inject constructor(
    private val commonDataService: CommonDataService
) : BaseViewModel<CallsViewState>() {

    fun onCallSecurityClicked() {
        _viewCommands.onNext(CallNumberViewCommand(commonDataService.securityPhone))
    }

    fun onCallParkingClicked() {
        _viewCommands.onNext(CallNumberViewCommand(commonDataService.parkingPhone))
    }
}