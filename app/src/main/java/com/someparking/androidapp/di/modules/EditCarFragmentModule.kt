package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.edit_car.EditCarBottomSheetDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class EditCarFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): EditCarBottomSheetDialogFragment
}