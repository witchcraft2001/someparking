package com.someparking.androidapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.someparking.androidapp.core.base.BaseActivity
import com.someparking.androidapp.databinding.ActivityProfileBinding
import com.someparking.androidapp.domain.ProfileActivityMode
import com.someparking.androidapp.fragments.password_change.PasswordChangeFragment
import com.someparking.androidapp.fragments.personal_data.PersonalDataFragment

class ProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val activityMode: ProfileActivityMode
        get() = intent.getSerializableExtra(ARG_ACTIVITY_MODE) as? ProfileActivityMode
                ?: ProfileActivityMode.PERSONAL_DATA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(
                            android.R.id.content,
                            when (activityMode) {
                                ProfileActivityMode.PERSONAL_DATA -> PersonalDataFragment.newInstance()
                                ProfileActivityMode.PASSWORD -> PasswordChangeFragment.newInstance()
                                else -> PersonalDataFragment.newInstance()
                            }
                    )
                    .commitNow()
        }
    }

    companion object {
        const val ARG_ACTIVITY_MODE = "ARG_ACTIVITY_MODE"

        fun newInstance(context: Context, mode: ProfileActivityMode) =
                Intent(context, ProfileActivity::class.java).apply {
                    putExtra(ARG_ACTIVITY_MODE, mode)
                }
    }

}