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

        // ซ่อน ZZZ (ที่เราเพิ่งเพิ่มใน XML) ไว้ก่อน
        binding.tvZzz.visibility = View.GONE

        updateLightUI()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        loadPetState()
    }

    private fun loadPetState() {
        val happiness = PetManager.happiness
        val hunger = PetManager.hunger
        val energy = PetManager.energy

        // *** แก้ไขแล้ว (1) ***
        binding.textViewPetName.text = PetManager.petName

        binding.progressBarPlay.progress = happiness
        binding.progressBarFood.progress = hunger
        binding.progressBarSleep.progress = energy

        updateAllBarColors(happiness, hunger, energy)
    }

    private fun setupListeners() {
        val viewPager = (activity as MainActivity).binding.viewPager

        // *** แก้ไขแล้ว (2) ***
        binding.buttonArrowLeft.setOnClickListener {
            if (isSleeping) return@setOnClickListener
            (activity as MainActivity).binding.viewPager.currentItem = 1 // ไปหน้า Eat
        }
        // *** แก้ไขแล้ว (3) ***
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
        isSleeping = true

        if (isLightsOn) {
            isLightsOn = false
            updateLightUI()
        }

        // *** แก้ไขแล้ว (4) ***
        // นี่คือ id ของรูป Peanut
        binding.imageViewPeanut.animate().translationY(150f).setDuration(500).start()

        // นี่คือ id ของ Zzz ที่เราเพิ่งเพิ่มใน XML
        binding.tvZzz.visibility = View.VISIBLE

        PetManager.energy += 40
        PetManager.hunger -= 10
        loadPetState()

        Handler(Looper.getMainLooper()).postDelayed({
            wakeUp()
        }, 3000)
    }

    private fun wakeUp() {
        isSleeping = false

        // *** แก้ไขแล้ว (5) ***
        binding.imageViewPeanut.animate().translationY(0f).setDuration(500).start()
        binding.tvZzz.visibility = View.GONE

        loadPetState()
    }

    private fun toggleLights() {
        isLightsOn = !isLightsOn
        updateLightUI()

        if (!isLightsOn) {
            PetManager.energy += 10
            loadPetState()
        }
    }

    // ฟังก์ชันนี้ OK แล้ว เพราะ XML ของคุณมี @color/sleep_background_light
    private fun updateLightUI() {
        val colorRes = if (isLightsOn) {
            R.color.sleep_background_light
        } else {
            R.color.sleep_background_dark // 👈 (ต้องแน่ใจว่ามีสีนี้ใน colors.xml)
        }
        binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), colorRes))
    }


    // ฟังก์ชันนี้จะ "แดง" ถ้าคุณยังไม่ได้เพิ่มสีใน colors.xml
    private fun updateAllBarColors(play: Int, food: Int, sleep: Int) {
        // XML ของคุณใช้สีชมพู/เขียว/เหลือง แต่ Logic ของเราจะเปลี่ยนสีตามพลัง
        // เราจะยึดตาม Logic นี้
        updateBarColor(binding.progressBarPlay, play, R.color.status_green) // 👈 (ต้องมีสีนี้)
        updateBarColor(binding.progressBarFood, food, R.color.status_green)
        updateBarColor(binding.progressBarSleep, sleep, R.color.status_green)
    }

    // ฟังก์ชันนี้จะ "แดง" ถ้าคุณยังไม่ได้เพิ่มสีใน colors.xml
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