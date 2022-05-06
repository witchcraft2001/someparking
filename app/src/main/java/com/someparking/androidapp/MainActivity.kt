package com.someparking.androidapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.someparking.androidapp.core.base.BaseActivity
import com.someparking.androidapp.databinding.ActivityMainBinding
import com.someparking.androidapp.fragments.home.HomeFragment
import com.someparking.androidapp.navigation.BackButtonListener
import dagger.android.AndroidInjection

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    android.R.id.content,
                    HomeFragment.newInstance()
                )
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        var fragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                fragment = f
                break
            }
        }
        if (fragment != null && fragment is BackButtonListener
            && (fragment as BackButtonListener).onBackPressed()
        ) {
            return
        } else {
            super.onBackPressed()
        }
    }
}