package com.example.peanut

// 1. Import สิ่งที่จำเป็น
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

// 2. ระบุว่า Adapter นี้ต้องรับ "FragmentActivity" (ก็คือ MainActivity)
class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // 3. บอกว่ามีทั้งหมดกี่หน้า (Fragment)
    override fun getItemCount(): Int {
        return 3 // เรามี 3 ห้อง: Play, Eat, Sleep
    }

    // 4. บอกว่า "ตำแหน่ง" (position) ไหน คือ Fragment อะไร
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlayFragment()     // หน้าที่ 0 (หน้าแรก) คือ PlayFragment
            1 -> EatFragment()     // หน้าที่ 1 (หน้ากลาง) คือ EatFragment
            2 -> SleepFragment()    // หน้าที่ 2 (หน้าสุดท้าย) คือ SleepFragment
            else -> throw IllegalArgumentException("Invalid position") // กันพลาด
        }
    }
}