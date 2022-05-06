package com.someparking.androidapp.core.base

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A [BottomSheetDialogFragment] that injects its members in [onAttach] and can be used to inject
 * child [BottomSheetDialogFragment]s attached to it.
 * Note that when this fragment gets reattached, its members will be injected again.
 *
 * @author Ryan Amaral
 */
open class DaggerBottomSheetDialogFragment : BottomSheetDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector(): DispatchingAndroidInjector<Any> {
        return androidInjector
    }
}