package com.someparking.androidapp.fragments.add_car

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
import com.someparking.androidapp.databinding.FragmentAddCarBinding
import com.someparking.androidapp.domain.Message
import com.someparking.androidapp.fragments.edit_car.FinishCommand
import com.someparking.androidapp.navigation.BackButtonListener
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import com.someparking.androidapp.navigation.RouterProvider
import javax.inject.Inject

class AddCarFragment : BaseFragment(), BackButtonListener {
    private lateinit var binding: FragmentAddCarBinding

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelFactory: AddCarViewModel.Factory
    private val viewModel: AddCarViewModel by viewModels {
        viewModelFactory.get((parentFragment as RouterProvider).router)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCarBinding.inflate(layoutInflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun renderViewState(viewState: AddCarViewState) {
        with(binding) {
            progressBar.visible(viewState.isProgressVisible)
            groupForm.visible(!viewState.isProgressVisible)
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is ShowNetworkErrorCommand ->
                showMessage(Message.Dialog(getString(R.string.text_network_error)))
            is ShowTextErrorCommand -> showMessage(Message.Dialog(command.message))
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonAddCar.setThrottledClickListener { viewModel.onAddCarClicked() }
            inputCarnumber.addTextChangedListener { text -> viewModel.onCarnumberChanged(text.toString()) }
            inputBrand.addTextChangedListener { text -> viewModel.onBrandChanged(text.toString()) }
            inputModel.addTextChangedListener { text -> viewModel.onModelChanged(text.toString()) }
            inputComment.addTextChangedListener { text -> viewModel.onCommentChanged(text.toString()) }
        }
    }

    companion object {
        fun newInstance() = AddCarFragment()
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackClicked()
        return true
    }
}