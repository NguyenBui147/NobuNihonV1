package com.example.doanchuyende.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Adapters.KanjiAdapter
import com.example.doanchuyende.Models.KanjiModel
import com.example.doanchuyende.R

class KanjiActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KanjiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kanji)

        recyclerView = findViewById(R.id.recyclerViewKanji)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data
        val kanjiList = listOf(
            KanjiModel(
                "1",
                "日",
                "Nhật, Ngày",
                "ニチ、ジツ",
                "ひ、か",
                listOf("日本 (にほん) - Nhật Bản", "今日 (きょう) - Hôm nay"),

            ),
            KanjiModel(
                "2",
                "月",
                "Nguyệt, month",
                "ゲツ、ガツ",
                "つき",
                listOf("月曜日 (げつようび) - Thứ Hai", "一月 (いちがつ) - Tháng 1"),

            )

        )

        adapter = KanjiAdapter(kanjiList)
        recyclerView.adapter = adapter
    }
}