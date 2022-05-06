package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.personal_data.PersonalDataFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PersonalDataFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): PersonalDataFragment
}