package com.someparking.androidapp.di.components

import com.someparking.androidapp.SomeParkingApp
import com.someparking.androidapp.di.modules.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AndroidInjectionModule::class,
            AppModule::class,
            NavigationModule::class,
            HomeFragmentModule::class,
            PreferencesModule::class,
            MainActivityModule::class,
            MessageActivityModule::class,
            ProfileActivityModule::class,
            ProfileFragmentModule::class,
            TabContainerFragmentModule::class,
            CarsFragmentModule::class,
            BookingsFragmentModule::class,
            CallsBottomSheetDialogFragmentModule::class,
            MessageFragmentModule::class,
            AddCarFragmentModule::class,
            AddBookingFragmentModule::class,
            ShowBookingFragmentModule::class,
            EditCarFragmentModule::class
        ]
)
interface AppComponent {
    fun inject(application: SomeParkingApp)
}