package com.someparking.androidapp.fragments.show_booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseFragment
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.requireArgument
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentShowBookingBinding
import com.someparking.androidapp.domain.BookingData
import com.someparking.androidapp.domain.Message
import com.someparking.androidapp.navigation.BackButtonListener
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import com.someparking.androidapp.navigation.RouterProvider
import java.util.*
import javax.inject.Inject

class ShowBookingFragment : BaseFragment(), BackButtonListener {
    private lateinit var binding: FragmentShowBookingBinding

    private var startTime: Calendar = Calendar.getInstance()
    private var endTime: Calendar = Calendar.getInstance()

    private val params: BookingData by requireArgument(ARGS_BOOKING_DATA)

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelFactory: ShowBookingViewModel.Factory
    private val viewModel: ShowBookingViewModel by viewModels {
        viewModelFactory.get(params, (parentFragment as RouterProvider).router)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowBookingBinding.inflate(layoutInflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun renderViewState(viewState: ShowBookingViewState) {
        with(binding) {
            progressBar.visible(viewState.isProgressVisible)
            groupForm.visible(!viewState.isProgressVisible)
            tvTimerEarly.visible(viewState.isEarly && viewState.timer != null)
            layoutTimer.visible(viewState.isActive && viewState.timer != null)
            buttonCancelBooking.visible(viewState.isCancelButtonVisible)
            viewState.timer?.let {
                tvTimerEarly.text = getString(R.string.text_timer_early_format, it)
                tvTimer.text = it
            }
            viewState.booking?.let {
                it.parkingSpace?.also { space ->
                    tvParkingPlace.text = space
                    layoutParkingPlace.visible(true)
                } ?: layoutParkingPlace.visible(false)
                tvCarnumber.text = it.carNum
                tvStatus.text = it.status
                tvTitle.text = getString(R.string.text_parking_place_format, it.id)
            }
            tvDate.text = viewState.start?.let {
                getString(
                    R.string.text_date_format,
                    it.get(Calendar.DAY_OF_MONTH),
                    it.get(Calendar.MONTH) + 1,
                    it.get(Calendar.YEAR)
                )
            } ?: ""
            val start = viewState.start?.let {
                startTime = it
                getString(
                    R.string.text_time_format,
                    it.get(Calendar.HOUR_OF_DAY),
                    it.get(Calendar.MINUTE)
                )
            } ?: ""
            val end = viewState.end?.let {
                endTime = it
                getString(
                    R.string.text_time_format,
                    it.get(Calendar.HOUR_OF_DAY),
                    it.get(Calendar.MINUTE)
                )
            } ?: ""
            tvTime.text = String.format("%s - %s", start, end)
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
            buttonCancelBooking.setThrottledClickListener { viewModel.onCancelBookingClicked() }
        }
    }

    companion object {
        private const val ARGS_BOOKING_DATA = "ARGS_BOOKING_DATA"
        fun newInstance(bookingData: BookingData) =
            ShowBookingFragment().apply {
                arguments = Bundle(1).apply {
                    putParcelable(ARGS_BOOKING_DATA, bookingData)
                }
            }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackClicked()
        return true
    }
}