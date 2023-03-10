package com.example.wonderland.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wonderland.fragment.*

class SellerOrderListAdapter(fragment: FragmentActivity, private val user: String): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> SellerOrderListPendingFrag(user)
            1 -> SellerOrderListProcessingFrag(user)
            2 -> SellerOrderListCompleteFrag(user)
            3 -> SellerOrderListCanceledFrag(user)
            else -> {
                Fragment()
            }
        }
    }
}