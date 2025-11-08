package com.example.peanut

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.peanut.databinding.FragmentEatBinding

class EatFragment : Fragment() {

    private var _binding: FragmentEatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // ตั้งค่าปุ่มกดแค่ครั้งเดียว
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        // สั่งให้โหลดค่าพลัง (และอัปเดตสี) ทุกครั้ง
        loadPetState()
    }

    private fun loadPetState() {
        val happiness = PetManager.happiness
        val hunger = PetManager.hunger
        val energy = PetManager.energy

        // 2. อัปเดตชื่อ (ถ้ามี)
        // *** แก้ไขแล้ว (1) ***
        binding.textViewPetName.text = PetManager.petName

        // 3. อัปเดตตัวเลขในหลอด
        binding.progressBarPlay.progress = happiness
        binding.progressBarFood.progress = hunger
        binding.progressBarSleep.progress = energy

        // 4. สั่งอัปเดตสี
        updateAllBarColors(happiness, hunger, energy)
    }

    // Logic การกดปุ่ม
    private fun setupListeners() {
        // 1. ปุ่มลูกศร (เชื่อมกับ ViewPager ใน MainActivity)
        val viewPager = (activity as MainActivity).binding.viewPager

        // *** แก้ไขแล้ว (2) ***
        binding.buttonArrowLeft.setOnClickListener {
            (activity as MainActivity).binding.viewPager.currentItem = 0 // ไปหน้า Play (index 0)
        }
        // *** แก้ไขแล้ว (3) ***
        binding.buttonArrowRight.setOnClickListener {
            (activity as MainActivity).binding.viewPager.currentItem = 2 // ไปหน้า Sleep (index 2)
        }

        // 2. ปุ่มไอเท็ม (ID เหล่านี้ถูกต้องแล้วตามโค้ดเก่าของคุณ)
        binding.buttonWorm.setOnClickListener {
            PetManager.hunger += 20
            PetManager.happiness -= 5
            loadPetState()
        }
        binding.buttonSunset.setOnClickListener {
            PetManager.happiness += 5
            PetManager.hunger += 5
            loadPetState()
        }
        binding.buttonWater.setOnClickListener {
            PetManager.hunger += 5
            loadPetState()
        }
    }

    private fun updateAllBarColors(play: Int, food: Int, sleep: Int) {
        updateBarColor(binding.progressBarPlay, play, R.color.status_green)
        updateBarColor(binding.progressBarFood, food, R.color.status_green)
        updateBarColor(binding.progressBarSleep, sleep, R.color.status_green)
    }

    private fun updateBarColor(progressBar: ProgressBar, value: Int, defaultColorRes: Int) {
        val colorRes = when {
            value <= 20 -> R.color.status_red
            value <= 50 -> R.color.status_yellow
            else -> defaultColorRes
        }
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorRes))
        progressBar.progressTintList = colorStateList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}