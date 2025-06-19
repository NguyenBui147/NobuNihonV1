package com.example.doanchuyende.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Adapters.KanjiAdapter
import com.example.doanchuyende.Database.KanjiDatabaseHelper
import com.example.doanchuyende.R
import kotlinx.coroutines.*

class KanjiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KanjiAdapter
    private lateinit var dbHelper: KanjiDatabaseHelper
    private var loadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji)

        recyclerView = findViewById(R.id.recyclerViewKanji)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        dbHelper = KanjiDatabaseHelper(this)
        adapter = KanjiAdapter(mutableListOf())
        recyclerView.adapter = adapter

        // Hiển thị toàn bộ Kanji khi vào màn hình
        loadAllKanji()
    }

    private fun loadAllKanji() {
        loadJob?.cancel()
        loadJob = CoroutineScope(Dispatchers.IO).launch {
            val allKanji = dbHelper.getAllKanji()
            withContext(Dispatchers.Main) {
                recyclerView.post {
                    adapter.updateList(allKanji)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadJob?.cancel()
    }
}