package com.someparking.androidapp.di.modules

import com.someparking.androidapp.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesActivityAndroidInjector(): MainActivity
}