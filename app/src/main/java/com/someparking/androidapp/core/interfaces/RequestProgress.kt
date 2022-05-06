package com.someparking.androidapp.core.interfaces

interface RequestProgress {
    fun setProgressVisibility(isVisible: Boolean)
    fun showProgress()
    fun hideProgress()
}