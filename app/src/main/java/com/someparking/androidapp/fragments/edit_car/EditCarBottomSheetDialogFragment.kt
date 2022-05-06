package com.someparking.androidapp.fragments.edit_car

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseBottomSheetDialog
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.requireArgument
import com.someparking.androidapp.core.extensions.sendFragmentResult
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentEditCarBinding
import com.someparking.androidapp.domain.CarData
import com.someparking.androidapp.domain.Message
import timber.log.Timber
import javax.inject.Inject

class EditCarBottomSheetDialogFragment : BaseBottomSheetDialog() {
    @Inject
    lateinit var viewModelFactory: EditCarViewModel.Factory
    private val viewModel: EditCarViewModel by viewModels {
        viewModelFactory.get(params)
    }

    private lateinit var binding: FragmentEditCarBinding

    private val params: CarData by requireArgument(ARGS_CAR_DATA)

    override val layoutIdRes: Int
        get() = R.layout.fragment_calls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCarBinding.inflate(inflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun renderViewState(viewState: EditCarViewState) {
        with(binding) {
            progressBar.visible(viewState.isProgressVisible)
//            groupForm.visible(!viewState.isProgressVisible)
            updateTextEditIfNeed(inputBrand, viewState.brand)
            updateTextEditIfNeed(inputModel, viewState.model)
            updateTextEditIfNeed(inputCarnumber, viewState.carnumber)
            updateTextEditIfNeed(inputComment, viewState.comment)
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonBack.setThrottledClickListener { dismiss() }
            buttonSave.setThrottledClickListener { viewModel.onSaveButtonClicked() }
            buttonDelete.setThrottledClickListener { showDeleteDialog() }
            inputBrand.addTextChangedListener { text -> viewModel.onBrandChanged(text.toString()) }
            inputModel.addTextChangedListener { text -> viewModel.onModelChanged(text.toString()) }
            inputCarnumber.addTextChangedListener { text -> viewModel.onCarnumberChanged(text.toString()) }
            inputComment.addTextChangedListener { text -> viewModel.onCommentChanged(text.toString()) }
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.text_warning_title))
            .setMessage(getString(R.string.text_sure_to_remove_car_message))
            .setPositiveButton(getString(R.string.text_yes)) { dialog, _ ->
                viewModel.onDeleteButtonClicked()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.text_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is ShowNetworkErrorCommand ->
                showMessage(Message.Dialog(getString(R.string.text_network_error)))
            is ShowTextErrorCommand -> showMessage(Message.Dialog(command.message))
            is FinishCommand -> {
                sendFragmentResult(EDIT_CAR_DIALOG_OBSERVATION_KEY, DIALOG_RESULT to RESULT_ACCEPT)
                dismiss()
            }
        }
    }

    companion object {
        const val RESULT_ACCEPT = 1
        const val DIALOG_RESULT = "DIALOG_RESULT"
        const val EDIT_CAR_DIALOG_OBSERVATION_KEY = "EDIT_CAR_DIALOG_OBSERVATION_KEY"

        const val TAG = "EditCarBottomSheetDialogFragment"
        private const val ARGS_CAR_DATA = "ARGS_CAR_DATA"
        fun newInstance(item: CarData) =
                EditCarBottomSheetDialogFragment().apply {
                    arguments = Bundle(1).apply {
                        putParcelable(ARGS_CAR_DATA, item)
                    }
                }
    }
}