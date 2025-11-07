package com.example.peanut

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.peanut.databinding.ActivityMainBinding
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // 1. สร้างตัวแปรสำหรับ View Binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. ตั้งค่า View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. สร้าง Adapter
        val adapter = ViewPagerAdapter(this) // "this" หมายถึง MainActivity นี้

        // 4. นำ Adapter ไปเชื่อมกับ ViewPager2 ที่อยู่ใน XML
        binding.viewPager.adapter = adapter
    }
}