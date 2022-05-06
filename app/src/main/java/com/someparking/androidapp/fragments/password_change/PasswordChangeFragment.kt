package com.someparking.androidapp.fragments.password_change

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseFragment
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentPasswordChangeBinding
import com.someparking.androidapp.domain.Message
import javax.inject.Inject
import javax.inject.Provider

class PasswordChangeFragment : BaseFragment() {
    private lateinit var binding: FragmentPasswordChangeBinding

    @Inject
    lateinit var viewModelProvider: Provider<PasswordChangeViewModel>

    private val viewModel: PasswordChangeViewModel by viewModels { viewModelProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordChangeBinding.inflate(layoutInflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            buttonBack.setThrottledClickListener { viewModel.onBackClicked() }
            buttonSend.setThrottledClickListener { viewModel.onSendClicked() }
            inputFirstPassword.addTextChangedListener { text -> viewModel.onFirstPasswordChanged(text.toString()) }
            inputSecondPassword.addTextChangedListener { text -> viewModel.onSecondPasswordChanged(text.toString()) }
        }
    }

    private fun renderViewState(viewState: PasswordChangeViewState) {
        with(binding) {
            layoutForm.visible(!viewState.isProgressVisible)
            progressBar.visible(viewState.isProgressVisible)
            tvPasswordIncorrect.visible(viewState.isPasswordIncorrect)
            buttonSend.isEnabled =
                    !viewState.firstPassword.isNullOrEmpty() &&
                    !viewState.secondPassword.isNullOrEmpty() &&
                    !viewState.isPasswordIncorrect &&
                    viewState.isChanged
//            updateTextEditIfNeed(inputFirstPassword, viewState.firstPassword)
//            updateTextEditIfNeed(inputSecondPassword, viewState.secondPassword)
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is ShowNetworkErrorCommand ->
                showError(getString(R.string.text_network_error), true)
            is ShowTextErrorCommand -> showError(command.text, false)
            is ShowSuccessToastCommand -> showMessage(Message.Toast(getString(R.string.text_successfull_saved)))
            is FinishCommand -> requireActivity().finish()
        }
    }

    private fun showError(text: String, needClose: Boolean) {
        AlertDialog.Builder(requireContext())
            .setMessage(text)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.btn_ok)) { dialog, _ ->
                dialog.dismiss()
                if (needClose) {
                    requireActivity().finish()
                }
            }
            .show()
    }

    companion object {
        fun newInstance() = PasswordChangeFragment()
    }
}