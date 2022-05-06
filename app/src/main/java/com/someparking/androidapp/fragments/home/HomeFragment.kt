package com.someparking.androidapp.fragments.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseFragment
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentHomeBinding
import com.someparking.androidapp.domain.Message
import com.someparking.androidapp.fragments.calls.CallsBottomSheetDialogFragment
import com.someparking.androidapp.fragments.message.MessageActivity
import com.someparking.androidapp.navigation.*
import com.someparking.androidapp.navigation.Screens.Tab
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment : BaseFragment(), BackButtonListener {
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelProvider: Provider<HomeViewModel>
    private val viewModel: HomeViewModel by viewModels { viewModelProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.navigation.setOnItemSelectedListener { position ->
            when (position) {
                0 -> selectTab(BOOKING)
                1 -> selectTab(CARS)
                2 -> selectTab(PROFILE)
            }
        }
        if (savedInstanceState == null) {
            selectTab(BOOKING)
        }
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    fun setBackButtonVisible(visible: Boolean) {
        binding.layToolbar.buttonBack.visible(visible)
    }

    private fun renderViewState(viewState: HomeViewState) {
        with(binding.layToolbar) {
            buttonCall.visible(viewState.isBackButtonVisible)
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is OpenCallsDialogViewCommand -> openCallsDialog()
            is OpenUrlViewCommand -> openUrl(command.url)
            is OpenMessageViewCommand -> openMessageDialog()
        }
    }

    private fun openMessageDialog() {
        val intent = Intent(requireContext(), MessageActivity::class.java)
        startActivity(intent)
    }

    private fun openUrl(url: String) {
        val launchIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            startActivity(launchIntent)
        } catch (ex: ActivityNotFoundException) {
            Timber.e(ex)
            showMessage(Message.Dialog(getString(R.string.text_unable_to_open_web_browser)))
        }
    }

    private fun setListeners() {
        with(binding.layToolbar) {
            buttonCall.setThrottledClickListener { viewModel.onCallClicked() }
            buttonMarket.setThrottledClickListener { viewModel.onMarketClicked() }
            buttonMessage.setThrottledClickListener { viewModel.onMessageClicked() }
            buttonBack.setThrottledClickListener { onBackPressed() }
        }
    }

    private fun selectTab(tab: String) {
        val fm = childFragmentManager
        var currentFragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                currentFragment = f
                break
            }
        }
        val newFragment = fm.findFragmentByTag(tab)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
        val transaction = fm.beginTransaction()
        if (newFragment == null) {
            transaction.add(
                R.id.content_layout,
                Tab(tab).createFragment(fm.fragmentFactory), tab
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
            (newFragment as TabContainerFragment?)?.updateBackButton()
        }
        transaction.commitNow()
    }

    private fun openCallsDialog() {
        CallsBottomSheetDialogFragment.newInstance().show(childFragmentManager, "123")
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onBackPressed(): Boolean {
        var fragment: Fragment? = null
        val fragments = childFragmentManager.fragments
        for (f in fragments) {
            if (f.isVisible) {
                fragment = f
                break
            }
        }
        return (fragment != null && fragment is BackButtonListener &&
                (fragment as BackButtonListener).onBackPressed())
    }
}