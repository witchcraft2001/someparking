package com.someparking.androidapp.fragments.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.someparking.androidapp.core.base.AdapterOnClickListener
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.observeFragmentResult
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.domain.CarData
import com.someparking.androidapp.domain.models.CarModel
import com.someparking.androidapp.fragments.edit_car.EditCarBottomSheetDialogFragment
import com.someparking.androidapp.fragments.edit_car.EditCarBottomSheetDialogFragment.Companion.EDIT_CAR_DIALOG_OBSERVATION_KEY
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import com.someparking.androidapp.navigation.RouterProvider
import com.someparking.androidapp.databinding.FragmentCarsBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CarsFragment : DaggerFragment(), AdapterOnClickListener<CarModel> {
    private lateinit var binding: FragmentCarsBinding

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelFactory: CarsViewModel.Factory

    private val viewModel: CarsViewModel by viewModels { viewModelFactory.get((parentFragment as RouterProvider).router) }

    private lateinit var carsAdapter: CarListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarsBinding.inflate(layoutInflater)
        initRecyclerView()
        setListeners()
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        observeFragmentResult(EDIT_CAR_DIALOG_OBSERVATION_KEY) {
            viewModel.updateCars()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.onCarListChanged()
    }

    private fun setListeners() {
        with(binding) {
            buttonAddCar.setThrottledClickListener { viewModel.onAddCarClicked() }
        }
    }

    private fun renderViewState(viewState: CarsViewState) {
        with(binding) {
            progressBar.visible(viewState.isProgressVisible)
            rvCars.visible(viewState.isProgressVisible.not())
            carsAdapter.updateList(viewState.cars)
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
        when (command) {
            is ShowEditCarDialogCommand -> showEditDialog(command.item)
        }
    }

    private fun showEditDialog(item: CarData) {
        EditCarBottomSheetDialogFragment.newInstance(item)
                .show(childFragmentManager, EditCarBottomSheetDialogFragment.TAG)
    }

    private fun initRecyclerView() {
        carsAdapter = CarListAdapter(this)
        binding.rvCars.adapter = carsAdapter
        binding.rvCars.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvCars.setHasFixedSize(true)
    }

    companion object {
        fun newInstance() = CarsFragment()
    }

    override fun onClickItem(item: CarModel) {
        viewModel.onClickItem(item)
    }
}