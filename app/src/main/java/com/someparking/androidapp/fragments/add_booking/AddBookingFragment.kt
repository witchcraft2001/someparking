package com.someparking.androidapp.fragments.add_booking

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.BaseFragment
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentAddBookingBinding
import com.someparking.androidapp.domain.CarData
import com.someparking.androidapp.domain.CompanyData
import com.someparking.androidapp.domain.Message
import com.someparking.androidapp.navigation.BackButtonListener
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import com.someparking.androidapp.navigation.RouterProvider
import java.util.*
import javax.inject.Inject


class AddBookingFragment : BaseFragment(), BackButtonListener {
    private lateinit var binding: FragmentAddBookingBinding

    private var date: Calendar = Calendar.getInstance()
    private var startTime: Calendar = Calendar.getInstance()
    private var endTime: Calendar = Calendar.getInstance()

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelFactory: AddBookingViewModel.Factory
    private val viewModel: AddBookingViewModel by viewModels {
        viewModelFactory.get((parentFragment as RouterProvider).router)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBookingBinding.inflate(layoutInflater)
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        setListeners()
        return binding.root
    }

    private fun renderViewState(viewState: AddBookingViewState) {
        with(binding) {
            progressBar.visible(viewState.isProgressVisible)
            groupForm.visible(!viewState.isProgressVisible)
            layoutPlace.visible(viewState.isPlacesVisible)
            if (viewState.companies.isNotEmpty()) {
                initCompaniesSpinnerAdapter(viewState.companies)
            }
            if (viewState.cars.isNotEmpty()) {
                initCarsSpinnerAdapter(viewState.cars)
            }
            if (viewState.freePlaces.isNotEmpty()) {
                initPlaceSpinnerAdapter(viewState.freePlaces)
            }
            tvCommentIncorrect.visible(viewState.isCommentIncorrect)
            tvDateIncorrect.visible(viewState.isDateIncorrect)
            tvStartTimeIncorrect.visible(viewState.isStartTimeIncorrect)
            tvEndTimeIncorrect.visible(viewState.isEndTimeIncorrect)
            tvDate.text = viewState.date?.let {
                date = it
                getString(
                    R.string.text_date_format,
                    it.get(Calendar.DAY_OF_MONTH),
                    it.get(Calendar.MONTH) + 1,
                    it.get(Calendar.YEAR)
                )
            } ?: getString(R.string.text_select_parking_date)
            tvStart.text = viewState.start?.let {
                startTime = it
                getString(
                    R.string.text_time_format,
                    it.get(Calendar.HOUR_OF_DAY),
                    it.get(Calendar.MINUTE)
                )
            } ?: getString(R.string.text_select_time)
            tvEnd.text = viewState.end?.let {
                endTime = it
                getString(
                    R.string.text_time_format,
                    it.get(Calendar.HOUR_OF_DAY),
                    it.get(Calendar.MINUTE)
                )
            } ?: getString(R.string.text_select_time)
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
            buttonAddBooking.setThrottledClickListener { viewModel.onAddBookingClicked() }
            inputComment.addTextChangedListener { text -> viewModel.onCommentChanged(text.toString()) }
            layoutDate.setThrottledClickListener {
                setDate()
            }
            layoutStart.setThrottledClickListener {
                setTimeStart()
            }
            layoutEnd.setThrottledClickListener {
                setTimeEnd()
            }
        }
    }

    private val onStartTimeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
        startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        startTime.set(Calendar.MINUTE, minute)
        viewModel.onChangeTimeStart(startTime)
    }

    private val onEndTimeSetListener = OnTimeSetListener { _, hourOfDay, minute ->
        endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
        endTime.set(Calendar.MINUTE, minute)
        viewModel.onChangeTimeEnd(endTime)
    }

    private val onDateSetListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        date.set(Calendar.YEAR, year)
        date.set(Calendar.MONTH, monthOfYear)
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        viewModel.onChangeDate(date)
    }

    private fun setDate() {
        DatePickerDialog(
            requireContext(), onDateSetListener,
            startTime.get(Calendar.YEAR),
            startTime.get(Calendar.MONTH),
            startTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun setTimeStart() {
        TimePickerDialog(
            requireContext(), onStartTimeSetListener,
            startTime.get(Calendar.HOUR_OF_DAY),
            startTime.get(Calendar.MINUTE), true
        ).show()
    }

    fun setTimeEnd() {
        TimePickerDialog(
            requireContext(), onEndTimeSetListener,
            endTime.get(Calendar.HOUR_OF_DAY),
            endTime.get(Calendar.MINUTE), true
        ).show()
    }

    private fun initCompaniesSpinnerAdapter(items: List<CompanyData>) {
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

        binding.spinnerCompany.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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

    private fun initCarsSpinnerAdapter(items: List<CarData>) {
        if (binding.spinnerCar.adapter != null &&
            binding.spinnerCar.adapter.count == items.count()
        ) {
            return
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.spinnerCar.adapter = adapter

        binding.spinnerCar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onCarSelected(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initPlaceSpinnerAdapter(items: List<String>) {
        if (binding.spinnerParkingPlace.adapter != null &&
            binding.spinnerParkingPlace.adapter.count == items.count()
        ) {
            return
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        binding.spinnerParkingPlace.adapter = adapter

        binding.spinnerParkingPlace.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.onPlaceSelected(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }

    companion object {
        fun newInstance() = AddBookingFragment()
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackClicked()
        return true
    }
}