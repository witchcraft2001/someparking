package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.show_booking.ShowBookingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ShowBookingFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): ShowBookingFragment
}