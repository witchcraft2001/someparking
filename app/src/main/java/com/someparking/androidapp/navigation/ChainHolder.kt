package com.someparking.androidapp.navigation

import androidx.fragment.app.Fragment

interface ChainHolder {
    fun addToChain(fragment: Fragment)
    fun removeFromChain(fragment: Fragment)

    fun isChainEmpty(): Boolean
}