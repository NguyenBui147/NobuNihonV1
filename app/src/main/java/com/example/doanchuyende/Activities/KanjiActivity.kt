package com.example.doanchuyende.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Adapters.KanjiAdapter
import com.example.doanchuyende.Models.KanjiModel
import com.example.doanchuyende.R
import com.example.doanchuyende.Database.KanjiDatabaseHelper
import androidx.appcompat.widget.SearchView

class KanjiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KanjiAdapter
    private lateinit var kanjiList: MutableList<KanjiModel>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji)

        recyclerView = findViewById(R.id.recyclerViewKanji)
        val searchView = findViewById<SearchView>(R.id.searchView)
        Log.d("KanjiActivity", "RecyclerView found: "+(recyclerView != null))
        
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        Log.d("KanjiActivity", "GridLayoutManager set")

        try {
            // Initialize database helper and load kanji data
            val dbHelper = KanjiDatabaseHelper(this)
            kanjiList = dbHelper.getAllKanji().toMutableList()
            
            Log.d("KanjiActivity", "Loaded ${kanjiList.size} kanji items")
            if (kanjiList.isNotEmpty()) {
                Log.d("KanjiActivity", "First kanji: ${kanjiList[0].kanji}")
                Log.d("KanjiActivity", "First kanji meaning: ${kanjiList[0].meaning}")
            } else {
                Log.e("KanjiActivity", "No kanji items loaded!")
                Toast.makeText(this, "Không load được dữ liệu kanji", Toast.LENGTH_LONG).show()
            }

            adapter = KanjiAdapter(kanjiList)
            recyclerView.adapter = adapter
            
            Log.d("KanjiActivity", "Adapter set with ${adapter.itemCount} items")
            
            // Force refresh
            adapter.notifyDataSetChanged()

            // SearchView logic
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Optionally handle search button press
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val results = if (!newText.isNullOrEmpty()) {
                        dbHelper.searchKanji(newText)
                    } else {
                        dbHelper.getAllKanji()
                    }
                    adapter.updateList(results)
                    return true
                }
            })
        } catch (e: Exception) {
            Log.e("KanjiActivity", "Error loading kanji data", e)
            Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}