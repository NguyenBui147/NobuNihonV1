package com.example.doanchuyende.Activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.R
import com.example.doanchuyende.Database.KanjiDatabaseHelper
import com.example.doanchuyende.Views.KanjiDrawingView

class KanjiDetailActivity : AppCompatActivity() {
    private lateinit var kanjiTextView: TextView
    private lateinit var meaningTextView: TextView
    private lateinit var onyomiTextView: TextView
    private lateinit var kunyomiTextView: TextView
    private lateinit var examplesTextView: TextView
    
    // Drawing practice views
    private lateinit var kanjiDetailSection: LinearLayout
    private lateinit var drawingSection: LinearLayout
    private lateinit var practiceButton: Button
    private lateinit var clearButton: Button
    private lateinit var backButton: Button
    private lateinit var kanjiDrawingView: KanjiDrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji_detail)

        try {
            // Initialize detail views
            kanjiTextView = findViewById(R.id.kanjiTextView)
            meaningTextView = findViewById(R.id.meaningTextView)
            onyomiTextView = findViewById(R.id.onyomiTextView)
            kunyomiTextView = findViewById(R.id.kunyomiTextView)
            examplesTextView = findViewById(R.id.examplesTextView)
            
            // Initialize drawing practice views
            kanjiDetailSection = findViewById(R.id.kanjiDetailSection)
            drawingSection = findViewById(R.id.drawingSection)
            practiceButton = findViewById(R.id.practiceButton)
            clearButton = findViewById(R.id.clearButton)
            backButton = findViewById(R.id.backButton)
            kanjiDrawingView = findViewById(R.id.kanjiDrawingView)

            val kanjiId = intent.getStringExtra("KANJI_ID") ?: ""
            
            if (kanjiId.isNotEmpty()) {
                // Get kanji data from database helper
                val dbHelper = KanjiDatabaseHelper(this)
                val kanji = dbHelper.getKanjiById(kanjiId)
                
                if (kanji != null) {
                    kanjiTextView.text = kanji.kanji
                    meaningTextView.text = kanji.meaning
                    onyomiTextView.text = kanji.onyomi
                    kunyomiTextView.text = kanji.kunyomi
                    examplesTextView.text = kanji.examples.joinToString("\n")
                    
                    supportActionBar?.title = kanji.kanji
                } else {
                    Toast.makeText(this, "Không tìm thấy thông tin kanji", Toast.LENGTH_LONG).show()
                    finish()
                }
            } else {
                // Fallback to intent extras for backward compatibility
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
            }
            
            // Set up drawing practice functionality
            setupDrawingPractice()
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading kanji details: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun setupDrawingPractice() {
        practiceButton.setOnClickListener {
            // Switch to drawing mode
            kanjiDetailSection.visibility = View.GONE
            drawingSection.visibility = View.VISIBLE
            
            // Set the reference kanji for practice
            val currentKanji = kanjiTextView.text.toString()
            kanjiDrawingView.setReferenceKanji(currentKanji)
        }
        
        clearButton.setOnClickListener {
            // Clear the drawing canvas
            kanjiDrawingView.clear()
        }
        
        backButton.setOnClickListener {
            // Switch back to detail mode
            drawingSection.visibility = View.GONE
            kanjiDetailSection.visibility = View.VISIBLE
            // Clear the canvas when going back
            kanjiDrawingView.clear()
        }
    }
} 