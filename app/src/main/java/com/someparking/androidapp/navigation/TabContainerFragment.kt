package com.someparking.androidapp.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.someparking.androidapp.R
import com.someparking.androidapp.fragments.home.HomeFragment
import dagger.android.support.DaggerFragment
import java.lang.ref.WeakReference
import java.util.ArrayList
import javax.inject.Inject

class TabContainerFragment : DaggerFragment(), RouterProvider, BackButtonListener, ChainHolder {
    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.ftc_container, childFragmentManager)
    }

    @Inject
    lateinit var ciceroneHolder: LocalCiceroneHolder

    private val chain = ArrayList<WeakReference<Fragment>>()

    override val router: Router
        get() = cicerone.router

    private val containerName: String
        get() = requireArguments().getString(EXTRA_NAME) ?: ""

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerName)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab_container, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.ftc_container) == null) {
            cicerone.router.replaceScreen(getScreen())
        }
        updateBackButton()
    }

    private fun getScreen(): FragmentScreen {
        return when (containerName) {
            BOOKING -> Screens.BookingsScreen
            CARS -> Screens.CarsScreen
            PROFILE -> Screens.ProfileScreen
            else -> throw IllegalArgumentException("Unknown container $containerName")
        }
    }

    fun updateBackButton() {
        (parentFragment as HomeFragment?)?.setBackButtonVisible(isChainEmpty().not())
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.ftc_container)
        return if (fragment != null && fragment is BackButtonListener
            && (fragment as BackButtonListener).onBackPressed()
        ) {
            true
        } else {
            if (activity is RouterProvider) {
                (activity as RouterProvider).router.exit()
                true
            } else {
                false
            }
        }
    }

    override fun addToChain(fragment: Fragment) {
        chain.add(WeakReference<Fragment>(fragment))
        updateBackButton()
    }

    override fun removeFromChain(fragment: Fragment) {
        val it = chain.iterator()
        while (it.hasNext()) {
            val fragmentReference = it.next()
            val reference = fragmentReference.get()
            if (reference != null && reference === fragment) {
                it.remove()
                break
            }
        }
        updateBackButton()
    }

    override fun isChainEmpty(): Boolean =
        chain.isEmpty()

    companion object {
        private const val EXTRA_NAME = "navigation_extra_name"

        fun getNewInstance(name: String?) =
            TabContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_NAME, name)
                }
            }
    }
}