package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): ProfileFragment
}