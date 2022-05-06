package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.message.MessageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MessageFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): MessageFragment
}