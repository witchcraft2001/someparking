package com.someparking.androidapp.fragments.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.someparking.androidapp.core.base.AdapterOnClickListener
import com.someparking.androidapp.core.base.mvvm.ViewCommand
import com.someparking.androidapp.core.base.mvvm.observe
import com.someparking.androidapp.core.base.mvvm.viewModels
import com.someparking.androidapp.core.extensions.setThrottledClickListener
import com.someparking.androidapp.core.extensions.visible
import com.someparking.androidapp.databinding.FragmentBookingsBinding
import com.someparking.androidapp.domain.models.BookingModel
import com.someparking.androidapp.navigation.LocalCiceroneHolder
import com.someparking.androidapp.navigation.RouterProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookingsFragment : DaggerFragment(), AdapterOnClickListener<BookingModel> {
    private lateinit var binding: FragmentBookingsBinding

    @Inject
    lateinit var localCiceroneHolder: LocalCiceroneHolder

    @Inject
    lateinit var viewModelFactory: BookingsViewModel.Factory
    private val viewModel : BookingsViewModel by viewModels { viewModelFactory.get((parentFragment as RouterProvider).router) }

    private lateinit var bookingsAdapter: BookingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingsBinding.inflate(layoutInflater)
        initRecyclerView()
        setListeners()
        observe(viewModel.viewState, ::renderViewState)
        observe(viewModel.viewCommands, ::handleViewCommand)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateBookingList()
    }

    private fun setListeners() {
        with(binding) {
            buttonBooking.setThrottledClickListener { viewModel.onBookingClicked() }
        }
    }

    private fun renderViewState(viewState: BookingsViewState) {
        with(binding) {
            progressBar.visible(viewState.isProgressVisible)
            rvBookings.visible(viewState.isProgressVisible.not())
            bookingsAdapter.updateList(viewState.items)
        }
    }

    private fun handleViewCommand(command: ViewCommand) {
//        when (command) {
//        }
    }

    private fun initRecyclerView() {
        bookingsAdapter = BookingListAdapter(this)
        binding.rvBookings.adapter = bookingsAdapter
        binding.rvBookings.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvBookings.setHasFixedSize(true)
    }

    companion object {
        fun newInstance() = BookingsFragment()
    }

    override fun onClickItem(item: BookingModel) {
        viewModel.onItemClicked(item)
    }
}