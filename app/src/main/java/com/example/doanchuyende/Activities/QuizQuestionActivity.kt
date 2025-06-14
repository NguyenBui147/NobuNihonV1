package com.example.doanchuyende.Activities

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.doanchuyende.Models.Question
import com.example.doanchuyende.R
import com.google.android.material.progressindicator.LinearProgressIndicator

class QuizQuestionActivity : AppCompatActivity() {
    private lateinit var questionIndicator: TextView
    private lateinit var timerTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var btnAns1: Button
    private lateinit var btnAns2: Button
    private lateinit var btnAns3: Button
    private lateinit var btnAns4: Button
    private lateinit var btnNext: Button
    private lateinit var progressIndicator: LinearProgressIndicator

    private var currentQuestionIndex = 0
    private var selectedAnswer = -1
    private var score = 0
    private var timer: CountDownTimer? = null
    private val totalTime = 5 * 60 * 1000L
    private var timeLeft = totalTime
    private lateinit var quizId: String
    private lateinit var quizTitle: String

    private val questions = listOf(
        Question(1, "Hiragana của 'a' là gì?", listOf("あ", "い", "う", "え"), 0),
        Question(2, "Katakana của 'ka' là gì?", listOf("カ", "キ", "ク", "ケ"), 0),
        Question(3, "Từ vựng 'gakkou' nghĩa là gì?", listOf("Trường học", "Sách", "Bút", "Bàn"), 0),
        Question(4, "Ngữ pháp nào dùng để phủ định?", listOf("です", "じゃない", "ます", "が"), 1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        quizId = intent.getStringExtra("QUIZ_ID") ?: "1"
        quizTitle = intent.getStringExtra("QUIZ_TITLE") ?: "Quiz"

        questionIndicator = findViewById(R.id.question_indicator_textview)
        timerTextView = findViewById(R.id.timer_textview)
        questionTextView = findViewById(R.id.question_textView)
        btnAns1 = findViewById(R.id.btn_ans_1)
        btnAns2 = findViewById(R.id.btn_ans_2)
        btnAns3 = findViewById(R.id.btn_ans_3)
        btnAns4 = findViewById(R.id.btn_ans_4)
        btnNext = findViewById(R.id.btn_next)
        progressIndicator = findViewById(R.id.question_progress)

        supportActionBar?.title = quizTitle

        setListeners()
        startTimer()
        showQuestion()
    }

    private fun setListeners() {
        val answerButtons = listOf(btnAns1, btnAns2, btnAns3, btnAns4)
        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                selectAnswer(index)
            }
        }
        btnNext.setOnClickListener {
            if (selectedAnswer == -1) {
                Toast.makeText(this, "Hãy chọn một đáp án!", Toast.LENGTH_SHORT).show()
            } else {
                checkAnswer()
                goToNextQuestion()
            }
        }
    }

    private fun showQuestion() {
        val question = questions[currentQuestionIndex]
        questionIndicator.text = "Câu hỏi ${currentQuestionIndex + 1}/${questions.size}"
        questionTextView.text = question.question
        btnAns1.text = question.options[0]
        btnAns2.text = question.options[1]
        btnAns3.text = question.options[2]
        btnAns4.text = question.options[3]
        selectedAnswer = -1
        resetButtonColors()
        progressIndicator.progress = ((currentQuestionIndex + 1) * 100 / questions.size)
    }

    private fun selectAnswer(index: Int) {
        selectedAnswer = index
        resetButtonColors()
        when (index) {
            0 -> btnAns1.setBackgroundColor(getColor(R.color.orange_button))
            1 -> btnAns2.setBackgroundColor(getColor(R.color.orange_button))
            2 -> btnAns3.setBackgroundColor(getColor(R.color.orange_button))
            3 -> btnAns4.setBackgroundColor(getColor(R.color.orange_button))
        }
    }

    private fun resetButtonColors() {
        btnAns1.setBackgroundColor(getColor(R.color.gray))
        btnAns2.setBackgroundColor(getColor(R.color.gray))
        btnAns3.setBackgroundColor(getColor(R.color.gray))
        btnAns4.setBackgroundColor(getColor(R.color.gray))
    }

    private fun checkAnswer() {
        val correct = questions[currentQuestionIndex].correctAnswer
        if (selectedAnswer == correct) {
            score++
        }
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showQuestion()
        } else {
            timer?.cancel()
            Toast.makeText(this, "Hoàn thành! Điểm của bạn: $score/${questions.size}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                val minutes = (timeLeft / 1000) / 60
                val seconds = (timeLeft / 1000) % 60
                timerTextView.text = String.format("%d:%02d", minutes, seconds)
            }
            override fun onFinish() {
                Toast.makeText(this@QuizQuestionActivity, "Hết giờ! Điểm của bạn: $score/${questions.size}", Toast.LENGTH_LONG).show()
                finish()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
} 