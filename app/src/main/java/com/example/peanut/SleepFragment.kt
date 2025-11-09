package com.example.peanut

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.peanut.databinding.FragmentSleepBinding

class SleepFragment : Fragment() {

    private var _binding: FragmentSleepBinding? = null
    private val binding get() = _binding!!

    private var isLightsOn = true
    private var isSleeping = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ซ่อนZZZ
        binding.tvZzz.visibility = View.GONE

        updateLightUI()
        setupListeners()

        // รับhappy
        PetManager.happinessData.observe(viewLifecycleOwner) { newHappiness ->
            binding.progressBarPlay.progress = newHappiness
            updateBarColor(
                binding.progressBarPlay,
                newHappiness,
                R.color.status_green
            ) // (หรือสีเริ่มต้นของหลอดนี้)
        }
        // รับhunger
        PetManager.hungerData.observe(viewLifecycleOwner) { newHunger ->
            binding.progressBarFood.progress = newHunger
            updateBarColor(
                binding.progressBarFood,
                newHunger,
                R.color.status_green
            ) // (หรือสีเริ่มต้นของหลอดนี้)
        }
        // รับenergy
        PetManager.energyData.observe(viewLifecycleOwner) { newEnergy ->
            binding.progressBarSleep.progress = newEnergy
            updateBarColor(
                binding.progressBarSleep,
                newEnergy,
                R.color.status_green
            ) // (หรือสีเริ่มต้นของหลอดนี้)
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

    private fun setupListeners() {
        val viewPager = (activity as MainActivity).binding.viewPager

        binding.buttonArrowLeft.setOnClickListener { //ปุ่มหน้าซ้าย-ขวา
            if (isSleeping) return@setOnClickListener
            (activity as MainActivity).binding.viewPager.currentItem = 1 // ไปหน้า Eat
        }
        binding.buttonArrowRight.setOnClickListener {
            if (isSleeping) return@setOnClickListener
            (activity as MainActivity).binding.viewPager.currentItem = 0 // ไปหน้า Play
        }

        binding.buttonPillow.setOnClickListener {
            if (!isSleeping) {
                goToSleep()
            }
        }
        binding.buttonLamp.setOnClickListener {
            if (isSleeping) return@setOnClickListener
            toggleLights()
        }
    }

    private fun goToSleep() {
        //เงื่อนไขและค่าที่ฟื้นฟูจากการนอน
        isSleeping = true

        binding.imageViewPeanut.animate().translationY(150f).setDuration(500).start()
        binding.tvZzz.visibility = View.VISIBLE

        if (!isLightsOn) {
            PetManager.energy += 30
        } else {
            PetManager.energy += 5
        }

        PetManager.hunger -= 10
        loadPetState()

        Handler(Looper.getMainLooper()).postDelayed({
            wakeUp()
        }, 3000)
    }

    private fun wakeUp() {
        isSleeping = false

        binding.imageViewPeanut.animate().translationY(0f).setDuration(500).start()
        binding.tvZzz.visibility = View.GONE

        loadPetState()
    }

    private fun toggleLights() {
        //เปิด-ปิดไฟ
        isLightsOn = !isLightsOn
        updateLightUI()

        if (!isLightsOn) {
            PetManager.energy += 10
            loadPetState()
        }
    }

    private fun updateLightUI() {
        //เปลี่ยนสีพื้นหลัง-เปิดปิดไฟ
        val colorRes = if (isLightsOn) {
            R.color.sleep_background_light
        } else {
            R.color.sleep_background_dark
        }
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))
    }

    private fun updateAllBarColors(play: Int, food: Int, sleep: Int) {
        // แสดงผลสีบนหลอดต่ามค่าพลังที่เหลืออยู่
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
        val colorStateList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorRes))
        progressBar.progressTintList = colorStateList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}