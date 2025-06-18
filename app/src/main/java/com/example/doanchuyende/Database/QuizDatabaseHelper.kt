package com.example.doanchuyende.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.doanchuyende.Models.QuizModel
import com.example.doanchuyende.Models.Question
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_QUIZ = "quiz"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DURATION = "duration"
    }

    // Lưu trữ câu hỏi trong memory thay vì database
    private val hiraganaQuestions = mutableListOf<Question>()
    private val katakanaQuestions = mutableListOf<Question>()
    private val kanjiQuestions = mutableListOf<Question>()
    private val vocabularyQuestions = mutableListOf<Question>()

    init {
        // Load câu hỏi từ JSON khi khởi tạo
        loadAllQuestions(context)
    }

    private fun loadAllQuestions(context: Context) {
        loadQuestionsFromJson(context, "hiragana_questions.json", hiraganaQuestions)
        loadQuestionsFromJson(context, "katakana_questions.json", katakanaQuestions)
        loadQuestionsFromJson(context, "kanji_questions.json", kanjiQuestions)

    }

    private fun loadQuestionsFromJson(context: Context, fileName: String, questionList: MutableList<Question>) {
        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val question = Question(
                    id = i,
                    question = jsonObject.getString("question"),
                    options = listOf(
                        jsonObject.getJSONArray("options").getString(0),
                        jsonObject.getJSONArray("options").getString(1),
                        jsonObject.getJSONArray("options").getString(2),
                        jsonObject.getJSONArray("options").getString(3)
                    ),
                    correctAnswer = jsonObject.getInt("correctAnswer")
                )
                questionList.add(question)
            }
            Log.d("Database", "Loaded ${jsonArray.length()} questions from $fileName")
        } catch (e: IOException) {
            Log.e("Database", "Error reading JSON file $fileName: ${e.message}")
        } catch (e: Exception) {
            Log.e("Database", "Error parsing JSON file $fileName: ${e.message}")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val createQuizTable = """
            CREATE TABLE $TABLE_QUIZ (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DURATION TEXT
            )
        """.trimIndent()

            val createQuestionTable = """
            CREATE TABLE question (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                quiz_id TEXT,
                set_number INTEGER,
                question_text TEXT,
                option1 TEXT,
                option2 TEXT,
                option3 TEXT,
                option4 TEXT,
                correct_answer INTEGER
            )
        """.trimIndent()

            db.execSQL(createQuizTable)
            db.execSQL(createQuestionTable)
            Log.d("Database", "Quiz and Question tables created successfully")

        } catch (e: Exception) {
            Log.e("Database", "Error creating tables: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_QUIZ")
            onCreate(db)
            Log.d("Database", "Database upgraded successfully")
        } catch (e: Exception) {
            Log.e("Database", "Error upgrading database: ${e.message}")
        }
    }

    fun addQuiz(quiz: QuizModel) {
        try {
            val db = writableDatabase
            // Check if quiz already exists
            val cursor = db.query(
                TABLE_QUIZ,
                arrayOf(COLUMN_ID),
                "$COLUMN_ID = ?",
                arrayOf(quiz.id),
                null,
                null,
                null
            )

            if (cursor.count == 0) {
                val values = ContentValues().apply {
                    put(COLUMN_ID, quiz.id)
                    put(COLUMN_TITLE, quiz.title)
                    put(COLUMN_DESCRIPTION, quiz.description)
                    put(COLUMN_DURATION, quiz.duration)
                }
                val result = db.insert(TABLE_QUIZ, null, values)
                if (result != -1L) {
                    Log.d("Database", "Quiz added successfully: ${quiz.title}")
                } else {
                    Log.e("Database", "Failed to add quiz: ${quiz.title}")
                }
            } else {
                Log.d("Database", "Quiz already exists: ${quiz.title}")
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("Database", "Error adding quiz: ${e.message}")
        }
    }

    fun getAllQuizzes(): List<QuizModel> {
        val quizList = mutableListOf<QuizModel>()
        try {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_QUIZ", null)

            if (cursor.moveToFirst()) {
                do {
                    val quiz = QuizModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION))
                    )
                    quizList.add(quiz)
                    Log.d("Database", "Retrieved quiz: ${quiz.title}")
                } while (cursor.moveToNext())
            } else {
                Log.d("Database", "No quizzes found in database")
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("Database", "Error getting quizzes: ${e.message}")
        }
        return quizList
    }


    fun getRandomQuestionSet(quizId: String): List<Question> {
        val questions = when (quizId) {
            "1" -> hiraganaQuestions
            "2" -> katakanaQuestions
            "3" -> kanjiQuestions
            "4" -> vocabularyQuestions
            else -> emptyList()
        }
        
        // Random chọn câu hỏi từ danh sách có sẵn
        return when (quizId) {
            "3" -> { // Kanji Quiz: 20 câu hỏi
                if (questions.size >= 20) {
                    questions.shuffled().take(20)
                } else {
                    questions.shuffled()
                }
            }
            else -> { // Các quiz khác: 10 câu hỏi
                if (questions.size >= 10) {
                    questions.shuffled().take(10)
                } else {
                    questions.shuffled()
                }
            }
        }
    }

    fun insertQuestions(context: Context) {
        // Không cần làm gì vì câu hỏi đã được load trong init block
        Log.d("Database", "Questions loaded from JSON files")
    }
}