package com.example.doanchuyende.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Adapters.QuizListAdapter
import com.example.doanchuyende.Models.QuizModel
import com.example.doanchuyende.R

class QuizActivity : AppCompatActivity() {
    private lateinit var quizModelList: MutableList<QuizModel>
    private lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quizModelList = mutableListOf()
        setupRecyclerView()
        getQuizData()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewQuiz)
        adapter = QuizListAdapter(quizModelList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getQuizData() {
        quizModelList.add(QuizModel("1", "Hiragana Quiz", "Kiểm tra kiến thức về bảng chữ cái Hiragana", "5 phút"))
        quizModelList.add(QuizModel("2", "Katakana Quiz", "Kiểm tra kiến thức về bảng chữ cái Katakana", "5 phút"))
        quizModelList.add(QuizModel("3", "Từ vựng N5", "Kiểm tra từ vựng trình độ N5", "5 phút"))
        quizModelList.add(QuizModel("4", "Ngữ pháp N5", "Kiểm tra ngữ pháp trình độ N5", "5 phút"))
        quizModelList.add(QuizModel("5", "Kanji N5", "Kiểm tra Kanji N5", "5 phút"))
        adapter.notifyDataSetChanged()
    }
}