package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.bookings.BookingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BookingsFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): BookingsFragment
}