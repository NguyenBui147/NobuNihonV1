package com.example.doanchuyende.Activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.R

class VocabularyDetailActivity : AppCompatActivity() {
    private lateinit var japaneseTextView: TextView
    private lateinit var readingTextView: TextView
    private lateinit var meaningTextView: TextView
    private lateinit var exampleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocabulary_detail)

        // Initialize views
        japaneseTextView = findViewById(R.id.japaneseTextView)
        readingTextView = findViewById(R.id.readingTextView)
        meaningTextView = findViewById(R.id.meaningTextView)
        exampleTextView = findViewById(R.id.exampleTextView)

        // Get data from intent
        val japanese = intent.getStringExtra("JAPANESE") ?: ""
        val reading = intent.getStringExtra("READING") ?: ""
        val meaning = intent.getStringExtra("MEANING") ?: ""
        val example = intent.getStringExtra("EXAMPLE") ?: ""

        // Set data to views
        japaneseTextView.text = japanese
        readingTextView.text = reading
        meaningTextView.text = meaning
        exampleTextView.text = example

        // Set action bar title
        supportActionBar?.title = japanese
    }
} 