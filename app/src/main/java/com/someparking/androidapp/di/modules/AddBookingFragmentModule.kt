package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.add_booking.AddBookingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AddBookingFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): AddBookingFragment
}