package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.message.MessageActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MessageActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesActivityAndroidInjector(): MessageActivity
}