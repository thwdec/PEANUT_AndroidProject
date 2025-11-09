package com.example.peanut

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.peanut.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()

        // รับhappy
        PetManager.happinessData.observe(viewLifecycleOwner) { newHappiness ->
            binding.progressBarPlay.progress = newHappiness
            updateBarColor(binding.progressBarPlay, newHappiness, R.color.status_green) // (หรือสีเริ่มต้นของหลอดนี้)
        }
        // รับhunger
        PetManager.hungerData.observe(viewLifecycleOwner) { newHunger ->
            binding.progressBarFood.progress = newHunger
            updateBarColor(binding.progressBarFood, newHunger, R.color.status_green) // (หรือสีเริ่มต้นของหลอดนี้)
        }
        // รับenergy
        PetManager.energyData.observe(viewLifecycleOwner) { newEnergy ->
            binding.progressBarSleep.progress = newEnergy
            updateBarColor(binding.progressBarSleep, newEnergy, R.color.status_green) // (หรือสีเริ่มต้นของหลอดนี้)
        }
    }

    override fun onResume() {
        super.onResume()
        loadPetState()
    }

    private fun loadPetState() {
        val happiness = PetManager.happiness
        val hunger = PetManager.hunger
        val energy = PetManager.energy

        binding.textViewPetName.text = PetManager.petName //ชื่อ

        binding.progressBarPlay.progress = happiness //ค่าพลังงาน
        binding.progressBarFood.progress = hunger
        binding.progressBarSleep.progress = energy

        updateAllBarColors(happiness, hunger, energy)
    }

    private fun setupListeners() {
//ปุ่มเปลี่ยนหน้า
        val viewPager = (activity as MainActivity).binding.viewPager

        binding.buttonArrowLeft.setOnClickListener {
            (activity as MainActivity).binding.viewPager.currentItem = 2 // ไปหน้า Sleep (หน้าสุดท้าย)
        }
        binding.buttonArrowRight.setOnClickListener {
            (activity as MainActivity).binding.viewPager.currentItem = 1 // ไปหน้า Eat
        }


        binding.buttonBall.setOnClickListener {
            PetManager.happiness += 15
            PetManager.energy -= 10
            loadPetState()
        }
        binding.buttonMusic.setOnClickListener {
            PetManager.happiness += 10
            PetManager.energy -= 5
            loadPetState()
        }
        binding.buttonIpad.setOnClickListener {
            PetManager.happiness += 5
            PetManager.energy -= 2
            loadPetState()
        }
    }

    // แสดงผลสีบนหลอดต่ามค่าพลังที่เหลืออยู่
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