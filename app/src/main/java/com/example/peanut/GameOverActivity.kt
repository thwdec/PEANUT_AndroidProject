package com.example.peanut

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.peanut.databinding.ActivityGameOverBinding
import android.graphics.ColorMatrixColorFilter
import android.graphics.ColorMatrix

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        PetManager.onGameOver.postValue(false)

        binding = ActivityGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // เปลี่ยนถั่วเป็นสีเทา
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        binding.imageViewDeadPeanut.colorFilter = filter

        binding.buttonTryAgain.setOnClickListener {
            // Intent to Name
            val intent = Intent(this, NameActivity::class.java)

            // clear
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}