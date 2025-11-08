package com.example.peanut

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.peanut.databinding.ActivityNameBinding

class NameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PetManager.init(this.applicationContext)


        binding.buttonSubmit.setOnClickListener {

            val name = binding.editTextName.text.toString()

            if (name.isBlank()) {
                Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show()
            } else {
                PetManager.startNewGame(name)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}