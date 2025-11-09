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

        binding.textViewPetName.text = PetManager.petName
        binding.progressBarPlay.progress = happiness
        binding.progressBarFood.progress = hunger
        binding.progressBarSleep.progress = energy

        updateAllBarColors(happiness, hunger, energy)
    }

    // ปุ่ม
    private fun setupListeners() {

        val viewPager = (activity as MainActivity).binding.viewPager

        binding.buttonArrowLeft.setOnClickListener {
            (activity as MainActivity).binding.viewPager.currentItem = 0 // ไปหน้า Play (index 0)
        }
        binding.buttonArrowRight.setOnClickListener {
            (activity as MainActivity).binding.viewPager.currentItem = 2 // ไปหน้า Sleep (index 2)
        }

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