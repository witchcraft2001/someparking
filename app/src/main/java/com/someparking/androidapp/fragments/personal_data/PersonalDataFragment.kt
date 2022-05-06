package com.someparking.androidapp.fragments.personal_data

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
import com.someparking.androidapp.databinding.FragmentPersonalDataBinding
import com.someparking.androidapp.domain.Message
import javax.inject.Inject
import javax.inject.Provider

class PersonalDataFragment : BaseFragment() {
    private lateinit var binding: FragmentPersonalDataBinding

    @Inject
    lateinit var viewModelProvider: Provider<PersonalDataViewModel>

    private val viewModel: PersonalDataViewModel by viewModels { viewModelProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalDataBinding.inflate(layoutInflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            buttonBack.setThrottledClickListener { viewModel.onBackClicked() }
            buttonSend.setThrottledClickListener { viewModel.onSendClicked() }
            inputName.addTextChangedListener { text -> viewModel.onNameChanged(text.toString()) }
            inputSurname.addTextChangedListener { text -> viewModel.onSurnameChanged(text.toString()) }
            inputMidname.addTextChangedListener { text -> viewModel.onMidnameChanged(text.toString()) }
        }
    }

    private fun renderViewState(viewState: PersonalDataViewState) {
        with(binding) {
            layoutForm.visible(!viewState.isProgressVisible)
            progressBar.visible(viewState.isProgressVisible)
            tvNameIncorrect.visible(viewState.isNameIncorrect)
            tvSurnameIncorrect.visible(viewState.isSurnameIncorrect)
            tvMidnameIncorrect.visible(viewState.isMidnameIncorrect)
            buttonSend.isEnabled = !viewState.isMidnameIncorrect &&
                    !viewState.isNameIncorrect &&
                    !viewState.isSurnameIncorrect &&
                    viewState.isChanged
            updateTextEditIfNeed(inputName, viewState.name)
            updateTextEditIfNeed(inputSurname, viewState.surname)
            updateTextEditIfNeed(inputMidname, viewState.midname)
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
        fun newInstance() = PersonalDataFragment()
    }
}