package com.example.doanchuyende.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Adapters.KanjiAdapter
import com.example.doanchuyende.Database.KanjiDatabaseHelper
import com.example.doanchuyende.Models.KanjiModel
import com.example.doanchuyende.R
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class KanjiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KanjiAdapter
    private lateinit var dbHelper: KanjiDatabaseHelper
    private var searchJob: Job? = null // To manage coroutines
    private val debounceDuration = 300L // Delay in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji)

        recyclerView = findViewById(R.id.recyclerViewKanji)
        val searchView = findViewById<SearchView>(R.id.searchView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        dbHelper = KanjiDatabaseHelper(this)
        adapter = KanjiAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // Load all kanji initially
        loadAllKanji()

        // SearchView logic
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    performSearch(query)
                } else {
                    loadAllKanji()
                    Toast.makeText(this@KanjiActivity, "Enter a search query", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Cancel previous search job
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.IO).launch {
                    // Debounce: wait before executing search
                    delay(debounceDuration)
                    val results = if (!newText.isNullOrEmpty()) {
                        dbHelper.searchKanji(newText.trim())
                    } else {
                        dbHelper.getAllKanji()
                    }
                    withContext(Dispatchers.Main) {
                        if (results.isEmpty() && !newText.isNullOrEmpty()) {
                            Toast.makeText(this@KanjiActivity, "No results found for '$newText'", Toast.LENGTH_SHORT).show()
                        }
                        adapter.updateList(results)
                    }
                }
                return true
            }
        })

        // Clear query on close
        searchView.setOnCloseListener {
            loadAllKanji()
            false
        }
    }

    private fun loadAllKanji() {
        searchJob?.cancel() // Cancel any ongoing search
        searchJob = CoroutineScope(Dispatchers.IO).launch {
            val allKanji = dbHelper.getAllKanji()
            withContext(Dispatchers.Main) {
                adapter.updateList(allKanji)
            }
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel() // Cancel any ongoing search
        searchJob = CoroutineScope(Dispatchers.IO).launch {
            val results = dbHelper.searchKanji(query.trim())
            withContext(Dispatchers.Main) {
                if (results.isEmpty()) {
                    Toast.makeText(this@KanjiActivity, "No results found for '$query'", Toast.LENGTH_SHORT).show()
                }
                adapter.updateList(results)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchJob?.cancel() // Clean up coroutines
    }
}