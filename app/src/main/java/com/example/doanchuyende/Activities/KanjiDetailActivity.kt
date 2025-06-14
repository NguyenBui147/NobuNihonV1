package com.example.doanchuyende.Activities

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.R

class KanjiDetailActivity : AppCompatActivity() {
    private lateinit var kanjiTextView: TextView
    private lateinit var meaningTextView: TextView
    private lateinit var onyomiTextView: TextView
    private lateinit var kunyomiTextView: TextView
    private lateinit var examplesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_detail)

        try {
            kanjiTextView = findViewById(R.id.kanjiTextView)
            meaningTextView = findViewById(R.id.meaningTextView)
            onyomiTextView = findViewById(R.id.onyomiTextView)
            kunyomiTextView = findViewById(R.id.kunyomiTextView)
            examplesTextView = findViewById(R.id.examplesTextView)

            val kanji = intent.getStringExtra("KANJI") ?: ""
            val meaning = intent.getStringExtra("MEANING") ?: ""
            val onyomi = intent.getStringExtra("ONYOMI") ?: ""
            val kunyomi = intent.getStringExtra("KUNYOMI") ?: ""
            val examples = intent.getStringArrayListExtra("EXAMPLES") ?: arrayListOf()

            kanjiTextView.text = kanji
            meaningTextView.text = meaning
            onyomiTextView.text = onyomi
            kunyomiTextView.text = kunyomi
            examplesTextView.text = examples.joinToString("\n")

            supportActionBar?.title = kanji
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading kanji details: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
} 