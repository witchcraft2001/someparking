package com.someparking.androidapp.di.modules

import com.someparking.androidapp.ProfileActivity
import com.someparking.androidapp.di.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileActivityModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [
        PersonalDataFragmentModule::class,
        PasswordChangeFragmentModule::class,
    ])
    abstract fun contributesActivityAndroidInjector(): ProfileActivity
}