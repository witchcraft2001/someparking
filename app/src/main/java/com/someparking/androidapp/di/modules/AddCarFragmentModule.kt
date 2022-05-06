package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.add_car.AddCarFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddCarFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): AddCarFragment
}