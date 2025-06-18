package com.example.doanchuyende.Database

import android.content.Context
import android.util.Log
import com.example.doanchuyende.Models.KanjiModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class KanjiDatabaseHelper(private val context: Context) {
    
    // Store kanji data in memory
    private val kanjiList = mutableListOf<KanjiModel>()

    init {
        // Load kanji data from JSON when initialized
        loadKanjiFromJson()
    }

    private fun loadKanjiFromJson() {
        try {
            val jsonString = context.assets.open("kanji_list.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val examplesArray = jsonObject.getJSONArray("examples")
                val examples = mutableListOf<String>()
                
                for (j in 0 until examplesArray.length()) {
                    examples.add(examplesArray.getString(j))
                }
                
                val kanji = KanjiModel(
                    id = jsonObject.getString("id"),
                    kanji = jsonObject.getString("kanji"),
                    meaning = jsonObject.getString("meaning"),
                    onyomi = jsonObject.getString("onyomi"),
                    kunyomi = jsonObject.getString("kunyomi"),
                    examples = examples
                )
                kanjiList.add(kanji)
            }
            Log.d("KanjiDatabase", "Loaded ${jsonArray.length()} kanji from kanji_list.json")
        } catch (e: IOException) {
            Log.e("KanjiDatabase", "Error reading JSON file kanji_list.json: ${e.message}")
        } catch (e: Exception) {
            Log.e("KanjiDatabase", "Error parsing JSON file kanji_list.json: ${e.message}")
        }
    }

    fun getAllKanji(): List<KanjiModel> {
        return kanjiList.toList()
    }

    fun getKanjiById(id: String): KanjiModel? {
        return kanjiList.find { it.id == id }
    }

    fun searchKanji(query: String): List<KanjiModel> {
        return kanjiList.filter { 
            it.kanji.contains(query, ignoreCase = true) ||
            it.meaning.contains(query, ignoreCase = true) ||
            it.onyomi.contains(query, ignoreCase = true) ||
            it.kunyomi.contains(query, ignoreCase = true)
        }
    }
} 