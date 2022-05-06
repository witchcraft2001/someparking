package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.cars.CarsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CarsFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): CarsFragment
}