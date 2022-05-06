package com.someparking.androidapp.di.modules

import com.someparking.androidapp.fragments.calls.CallsBottomSheetDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CallsBottomSheetDialogFragmentModule {
    @ContributesAndroidInjector
    internal abstract fun bindFragment(): CallsBottomSheetDialogFragment
}