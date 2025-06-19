package com.example.doanchuyende.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnKana.setOnClickListener {
            startActivity(Intent(this, KanaAlphabetActivity::class.java))
        }
        binding.btnKanji.setOnClickListener {
            startActivity(Intent(this, KanjiActivity::class.java))
        }
        binding.btnFlashcard.setOnClickListener {
            startActivity(Intent(this, FlashcardActivity::class.java))
        }
        binding.btnQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
