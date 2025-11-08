package com.example.peanut

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.peanut.databinding.ActivityMainBinding
import android.content.Intent

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ViewPagerAdapter(this)

        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = true

        // chech onGameOver at PetManager
        PetManager.onGameOver.observe(this) { isGameOver ->
            if (isGameOver) {
                // Intent to GameOver page
                PetManager.onGameOver.postValue(false)

                val intent = Intent(this, GameOverActivity::class.java)
                startActivity(intent)

                finish()
            }
        }
    }
}