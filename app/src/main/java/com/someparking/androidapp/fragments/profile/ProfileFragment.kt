package com.someparking.androidapp.fragments.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.someparking.androidapp.ProfileActivity
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.databinding.FragmentProfileBinding
import com.someparking.androidapp.domain.ProfileActivityMode
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Provider

class ProfileFragment : DaggerFragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelProvider: Provider<ProfileViewModel>
    private val viewModel: ProfileViewModel by viewModels { viewModelProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            layoutPersonalData.setThrottledClickListener { viewModel.onPersonalDataClicked() }
            layoutContactData.setThrottledClickListener { viewModel.onContactDataClicked() }
            layoutPassword.setThrottledClickListener { viewModel.onPasswordClicked() }
            buttonLogout.setThrottledClickListener { showLogoutDialog() }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.text_warning_title))
                .setMessage(getString(R.string.text_sure_to_logout_message))
                .setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
                    viewModel.onLogoutClicked()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.text_no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is OpenPersonalDataViewCommand -> openPersonalDataView(ProfileActivityMode.PERSONAL_DATA)
            is OpenPasswordChangeViewCommand -> openPersonalDataView(ProfileActivityMode.PASSWORD)
            is RestartApplicationViewCommand -> {}
        }
    }

    private fun openPersonalDataView(mode: ProfileActivityMode) {
        val intent = ProfileActivity.newInstance(requireContext(), mode)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}