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
import com.example.doanchuyende.Database.QuizDatabaseHelper


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
        val dbHelper= QuizDatabaseHelper(this)
        dbHelper.addQuiz(QuizModel("1", "Hiragana Quiz", "Kiểm tra kiến thức về Hiragana", "5 phút"))
        dbHelper.addQuiz(QuizModel("2", "Katakana Quiz", "Kiểm tra kiến thức về Katakana", "5 phút"))
        dbHelper.addQuiz(QuizModel("3", "Kanji Quiz", "Kiểm tra kiến thức về Kanji", "10 phút"))
        dbHelper.addQuiz(QuizModel("4", "Từ vựng Quiz", "Kiểm tra kiến thức về từ vựng tiếng nhật", "10 phút"))
        
        // Thêm câu hỏi mẫu cho Hiragana Quiz
        dbHelper.insertSampleQuestions(this)
        
        quizModelList.clear()
        quizModelList.addAll(dbHelper.getAllQuizzes())
        adapter.notifyDataSetChanged()
    }



}