package com.someparking.androidapp.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import java.util.HashMap
import javax.inject.Inject

class LocalCiceroneHolder @Inject constructor() {
    private val containers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> =
        containers.getOrPut(containerTag) {
            Cicerone.create()
        }
}