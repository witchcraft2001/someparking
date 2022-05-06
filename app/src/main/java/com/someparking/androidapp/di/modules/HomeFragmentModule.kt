package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): HomeFragment
}