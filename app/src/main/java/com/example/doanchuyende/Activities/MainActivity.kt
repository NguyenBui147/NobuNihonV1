package com.example.doanchuyende.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

            setupClickListeners()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        try {
        binding.btnKana.setOnClickListener {
            startActivity(Intent(this, KanaAlphabetActivity::class.java))
        }
        binding.btnKanji.setOnClickListener {
            startActivity(Intent(this, KanjiActivity::class.java))
        }
        binding.btnVolcabulary.setOnClickListener {
            startActivity(Intent(this, VocabularyActivity::class.java))
        }
        binding.btnQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up click listeners: ${e.message}", e)
            Toast.makeText(this, "Error setting up buttons: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onDestroy: ${e.message}", e)
        }
    }
}
