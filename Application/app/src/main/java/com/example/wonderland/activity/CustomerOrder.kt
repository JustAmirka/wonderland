package com.example.wonderland.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wonderland.adapter.CustomerOrderAdapter
import com.example.wonderland.databinding.ActivityCustomerOrderBinding
import com.google.android.material.tabs.TabLayoutMediator

class CustomerOrder : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.customerOrderViewPager2.adapter = CustomerOrderAdapter(this)

        TabLayoutMediator(binding.customerOrderTabLayout, binding.customerOrderViewPager2
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Pending"
                1 -> tab.text = "Preparing"
                2 -> tab.text = "Arriving"
                3 -> tab.text = "Completed"
                4 -> tab.text = "Canceled"
            }
        }.attach()

    }
}