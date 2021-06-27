package com.example.bookingflats.features.flats.screens.main

import android.os.Bundle
import com.example.bookingflats.basemodule.base.platform.BaseActivity
import com.example.bookingflats.basemodule.utils.viewbinding.viewBinding
import com.example.bookingflats.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel>() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}