package com.someparking.androidapp.fragments.home

import com.someparking.androidapp.core.base.mvvm.BaseViewModel
import com.someparking.androidapp.services.CommonDataService
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val commonDataService: CommonDataService
) : BaseViewModel<HomeViewState>() {

    init {
        commonDataService.update()
            .subscribe()
            .safeSubscribe()
    }

    fun onCallClicked() {
        _viewCommands.onNext(OpenCallsDialogViewCommand)
    }

    fun onMessageClicked() {
        _viewCommands.onNext(OpenMessageViewCommand)
    }

    fun onMarketClicked() {
        _viewCommands.onNext(OpenUrlViewCommand(commonDataService.deliveryFoodLink))
    }
}