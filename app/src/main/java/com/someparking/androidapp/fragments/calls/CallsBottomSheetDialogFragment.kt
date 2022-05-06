package com.someparking.androidapp.fragments.calls

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseBottomSheetDialog
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.databinding.FragmentCallsBinding
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


class CallsBottomSheetDialogFragment : BaseBottomSheetDialog() {
    @Inject
    lateinit var viewModelProvider: Provider<CallsViewModel>
    private val viewModel: CallsViewModel by viewModels { viewModelProvider.get() }

    private lateinit var binding: FragmentCallsBinding

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
        binding = FragmentCallsBinding.inflate(inflater)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        with(binding) {
            layoutCancel.setThrottledClickListener { dismiss() }
            tvCallParking.setThrottledClickListener { viewModel.onCallParkingClicked() }
            tvCallSecurity.setThrottledClickListener { viewModel.onCallSecurityClicked() }
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is CallNumberViewCommand -> callNumber(command.number)
        }
    }

    private fun callNumber(number: String) {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),   //request specific permission from user
                10);
            return
        }
        try {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + if (number.startsWith('7')) "+$number" else number)
            startActivity(callIntent)
            dismiss()
        } catch (activityException: Exception) {
            Timber.e(activityException)
        }
    }

    companion object {
        fun newInstance(): CallsBottomSheetDialogFragment = CallsBottomSheetDialogFragment()
    }
}