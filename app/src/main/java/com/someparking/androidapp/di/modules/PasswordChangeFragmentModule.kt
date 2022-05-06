package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.password_change.PasswordChangeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PasswordChangeFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): PasswordChangeFragment
}