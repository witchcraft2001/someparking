package com.someparking.androidapp

import android.app.Application
import com.someparking.androidapp.di.components.DaggerAppComponent
import com.someparking.androidapp.di.modules.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class SomeParkingApp: Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        initTimber()
        DaggerAppComponent.builder().appModule(AppModule(this)).build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}