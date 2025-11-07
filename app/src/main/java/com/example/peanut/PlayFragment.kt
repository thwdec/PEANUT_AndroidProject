package com.example.peanut // <-- ต้องเป็นชื่อ package ของคุณ

// 1. Import สิ่งที่จำเป็น
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.peanut.databinding.FragmentPlayBinding // <-- Import คลาส Binding ที่ถูกสร้างอัตโนมัติ

class PlayFragment : Fragment() {

    // 2. สร้างตัวแปรสำหรับ View Binding
    //    (ชื่อ FragmentPlayBinding มาจากชื่อไฟล์ fragment_play.xml)
    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!! // ตัวแปรนี้ช่วยให้เราเรียกใช้ UI ได้ง่ายๆ

    // 3. แก้ไข onCreateView เพื่อใช้ View Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // สร้าง "ตัวเชื่อม" UI
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        // ส่ง "หน้าตา" (root view) กลับไป
        return binding.root
    }

    // 4. onViewCreated - ฟังก์ชันนี้จะทำงานหลังจาก UI ถูกสร้างเสร็จ
    //    นี่คือที่ที่เราจะเขียนลอจิกทั้งหมด (เช่น การคลิกปุ่ม)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- ตั้งค่าเริ่มต้น ---
        // เมื่อหน้าเปิดครั้งแรก ให้อัปเดตสี ProgressBar ทันที
        updatePlayProgress(70) // สมมติค่าเริ่มต้น
        updateFoodProgress(50) // สมมติค่าเริ่มต้น
        updateSleepProgress(80) // สมมติค่าเริ่มต้น

        // --- ตั้งค่าการคลิกปุ่ม ---
        binding.buttonBall.setOnClickListener {
            // เมื่อกดปุ่มบอล ให้เพิ่มค่า "เล่น" (Play)
            // เราจะเอาค่าปัจจุบันมาบวกเพิ่ม 10
            val currentProgress = binding.progressBarPlay.progress
            updatePlayProgress(currentProgress + 10)
        }

        binding.buttonMusic.setOnClickListener {
            // เมื่อกดปุ่มไมค์ ก็เพิ่มค่า "เล่น" (Play) เหมือนกัน
            val currentProgress = binding.progressBarPlay.progress
            updatePlayProgress(currentProgress + 15) // อาจจะเพิ่มไม่เท่ากัน
        }

        binding.buttonIpad.setOnClickListener {
            // เมื่อกดปุ่มไมค์ ก็เพิ่มค่า "เล่น" (Play) เหมือนกัน
            val currentProgress = binding.progressBarPlay.progress
            updatePlayProgress(currentProgress + 15) // อาจจะเพิ่มไม่เท่ากัน
        }
    }

    // 5. นี่คือฟังก์ชันที่เราจะใช้เปลี่ยนสี ProgressBar (สำหรับหลอด "Play")
    private fun updatePlayProgress(newValue: Int) {

        // กันไม่ให้ค่าเกิน 100 หรือต่ำกว่า 0
        val finalValue = newValue.coerceIn(0, 100)

        // 5.1 อัปเดตค่าตัวเลขในหลอด
        binding.progressBarPlay.progress = finalValue

        // 5.2 เลือกสีตามเงื่อนไข (n)
        val colorRes = when {
            finalValue <= 20 -> R.color.status_red     // ถ้าน้อยกว่าหรือเท่ากับ 20
            finalValue <= 50 -> R.color.status_yellow  // ถ้าน้อยกว่าหรือเท่ากับ 50 (และมากกว่า 20)
            else -> R.color.status_green               // ค่าอื่นๆ (มากกว่า 50)
        }

        // 5.3 สั่งเปลี่ยนสีหลอด (Tint)
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorRes))
        binding.progressBarPlay.progressTintList = colorStateList
    }

    // 6. (ตัวอย่าง) คุณสามารถสร้างฟังก์ชันสำหรับหลอดอื่นๆ ได้เหมือนกัน
    private fun updateFoodProgress(newValue: Int) {
        val finalValue = newValue.coerceIn(0, 100)
        binding.progressBarFood.progress = finalValue

        val colorRes = when {
            finalValue <= 20 -> R.color.status_red
            finalValue <= 50 -> R.color.status_yellow
            else -> R.color.status_green
        }
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorRes))
        binding.progressBarFood.progressTintList = colorStateList
    }

    private fun updateSleepProgress(newValue: Int) {
        val finalValue = newValue.coerceIn(0, 100)
        binding.progressBarSleep.progress = finalValue

        val colorRes = when {
            finalValue <= 20 -> R.color.status_red
            finalValue <= 50 -> R.color.status_yellow
            else -> R.color.status_green
        }
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorRes))
        binding.progressBarSleep.progressTintList = colorStateList
    }


    // 7. (สำคัญ) คืนค่า Binding เมื่อ Fragment ถูกทำลาย เพื่อป้องกัน Memory Leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}