package com.someparking.androidapp.fragments.message

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentMessageBinding
import com.someparking.androidapp.domain.FeedbackCompanyData
import com.someparking.androidapp.fragments.bookings.BookingsViewModel
import com.someparking.androidapp.fragments.personal_data.PersonalDataViewModel
import com.someparking.androidapp.navigation.RouterProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import javax.inject.Provider

class MessageFragment : DaggerFragment() {
    private lateinit var binding: FragmentMessageBinding

    @Inject
    lateinit var viewModelProvider: Provider<MessageViewModel>
    private val viewModel: MessageViewModel by viewModels { viewModelProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(layoutInflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            val listener = MaskedTextChangedListener("+7 ([000]) [000]-[00]-[00]", inputPhone)
            inputPhone.addTextChangedListener(listener)
            inputPhone.onFocusChangeListener = listener
            buttonBack.setThrottledClickListener { viewModel.onBackClicked() }
            buttonSend.setThrottledClickListener { viewModel.onSendClicked() }
            buttonThanks.setThrottledClickListener { viewModel.onThanksClicked() }
            inputName.addTextChangedListener { text -> viewModel.onNameChanged(text.toString()) }
            inputPhone.addTextChangedListener { text -> viewModel.onPhoneChanged(text.toString()) }
            inputText.addTextChangedListener { text -> viewModel.onTextChanged(text.toString()) }
            inputWorkplace.addTextChangedListener { text -> viewModel.onWorkplaceChanged(text.toString()) }
        }
    }

    private fun renderViewState(viewState: MessageViewState) {
        with(binding) {
            layoutForm.visible(viewState.isFormVisible)
            groupThanks.visible(viewState.isMessageSentVisible)
            progressBar.visible(viewState.isProgressVisible)
            tvNameIncorrect.visible(viewState.isNameIncorrect)
            tvPhoneIncorrect.visible(viewState.isPhoneIncorrect)
            tvTextIncorrect.visible(viewState.isTextIncorrect)
            tvWorkplaceIncorrect.visible(viewState.isWorkplaceIncorrect)
            updateTextEditIfNeed(inputName, viewState.name)
            updateTextEditIfNeed(inputPhone, viewState.phone)
            viewState.companies?.let {
                initCompaniesSpinnerAdapter(it)
            }
        }
    }

    private fun updateTextEditIfNeed(view: TextView, text: String?) {
        if (view.text.toString() != text ?: "") {
            view.text = text
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is ShowNetworkErrorCommand ->
                showError(getString(R.string.text_network_error), true)
            is ShowTextErrorCommand -> showError(command.text, false)
            is FinishMessageCommand -> requireActivity().finish()
        }
    }

    private fun initCompaniesSpinnerAdapter(items: List<FeedbackCompanyData>) {
        if (binding.spinnerCompany.adapter != null &&
            binding.spinnerCompany.adapter.count == items.count()
        ) {
            return
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.spinnerCompany.adapter = adapter

        binding.spinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onCompanySelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
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
        fun newInstance() = MessageFragment()
    }
}