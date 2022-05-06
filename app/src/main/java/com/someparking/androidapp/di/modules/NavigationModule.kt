package com.someparking.androidapp.di.modules

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {
    private val cicerone = Cicerone.create()

    @Singleton
    @Provides
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.getNavigatorHolder()

    @Singleton
    @Provides
    fun provideLocalCiceroneHolder(): LocalCiceroneHolder = LocalCiceroneHolder()

    @Singleton
    @Provides
    fun provideRouter(): Router = cicerone.router
}
