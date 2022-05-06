package com.someparking.androidapp.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.someparking.androidapp.domain.BookingData
import com.someparking.androidapp.fragments.add_booking.AddBookingFragment
import com.someparking.androidapp.fragments.add_car.AddCarFragment
import com.someparking.androidapp.fragments.bookings.BookingsFragment
import com.someparking.androidapp.fragments.cars.CarsFragment
import com.someparking.androidapp.fragments.home.HomeFragment
import com.someparking.androidapp.fragments.message.MessageFragment
import com.someparking.androidapp.fragments.profile.ProfileFragment
import com.someparking.androidapp.fragments.show_booking.ShowBookingFragment

const val BOOKING = "BOOKING"
const val CARS = "CARS"
const val PROFILE = "PROFILE"

object Screens {
    object HomeScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            HomeFragment.newInstance()
    }

    object ProfileScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            ProfileFragment.newInstance()
    }

    object BookingsScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            BookingsFragment.newInstance()
    }

    object CarsScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            CarsFragment.newInstance()
    }

    fun Tab(name: String) = FragmentScreen {
        TabContainerFragment.getNewInstance(name)
    }

    object MessageScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            MessageFragment.newInstance()
    }

    object AddCarScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            AddCarFragment.newInstance()
    }

    object AddBookingScreen : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            AddBookingFragment.newInstance()
    }

    class ShowBookingScreen(val data: BookingData) : FragmentScreen {
        override fun createFragment(factory: FragmentFactory): Fragment =
            ShowBookingFragment.newInstance(data)
    }
}