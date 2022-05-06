package com.someparking.androidapp.fragments.message

import android.os.Bundle
import com.someparking.androidapp.core.base.BaseActivity
import com.someparking.androidapp.databinding.ActivityMessageBinding

class MessageActivity : BaseActivity() {
    private lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    android.R.id.content,
                        MessageFragment.newInstance()
                )
                .commitNow()
        }
    }
}