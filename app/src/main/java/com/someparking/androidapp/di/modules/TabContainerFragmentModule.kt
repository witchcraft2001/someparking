package com.someparking.androidapp.di.modules

import com.someparking.androidapp.navigation.TabContainerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TabContainerFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): TabContainerFragment
}