package com.example.peanut

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlayFragment()
            1 -> EatFragment()
            2 -> SleepFragment()
            else -> PlayFragment() // ค่า default
        }
    }
}