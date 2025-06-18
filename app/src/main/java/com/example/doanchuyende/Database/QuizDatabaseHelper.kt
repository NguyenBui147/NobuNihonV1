package com.example.doanchuyende.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.doanchuyende.Models.QuizModel
import com.example.doanchuyende.Models.Question


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
        val setNumber = 1 // Luôn lấy set 1 vì chỉ có 1 set duy nhất
        val questions = mutableListOf<Question>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM question WHERE quiz_id = ? AND set_number = ?",
            arrayOf(quizId, setNumber.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                val q = Question(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    question = cursor.getString(cursor.getColumnIndexOrThrow("question_text")),
                    options = listOf(
                        cursor.getString(cursor.getColumnIndexOrThrow("option1")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option2")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option3")),
                        cursor.getString(cursor.getColumnIndexOrThrow("option4")),
                    ),
                    correctAnswer = cursor.getInt(cursor.getColumnIndexOrThrow("correct_answer"))
                )
                questions.add(q)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return questions
    }

    fun addQuestion(question: Question, quizId: String, setNumber: Int) {
        try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put("quiz_id", quizId)
                put("set_number", setNumber)
                put("question_text", question.question)
                put("option1", question.options[0])
                put("option2", question.options[1])
                put("option3", question.options[2])
                put("option4", question.options[3])
                put("correct_answer", question.correctAnswer)
            }
            val result = db.insert("question", null, values)
            if (result != -1L) {
                Log.d("Database", "Question added successfully: ${question.question}")
            } else {
                Log.e("Database", "Failed to add question: ${question.question}")
            }
        } catch (e: Exception) {
            Log.e("Database", "Error adding question: ${e.message}")
        }
    }

    fun insertSampleQuestions(context: Context) {
        val dbHelper = QuizDatabaseHelper(context)
        val sampleQuestions = listOf(
            "Hiragana của 'a' là gì?" to listOf("あ", "い", "う", "え"),
            "Hiragana của 'i' là gì?" to listOf("い", "え", "あ", "う"),
            "Hiragana của 'u' là gì?" to listOf("う", "あ", "お", "え"),
            "Hiragana của 'e' là gì?" to listOf("え", "お", "あ", "い"),
            "Hiragana của 'o' là gì?" to listOf("お", "う", "え", "あ"),
            "Hiragana của 'ka' là gì?" to listOf("か", "き", "く", "け"),
            "Hiragana của 'ki' là gì?" to listOf("き", "け", "こ", "く"),
            "Hiragana của 'ku' là gì?" to listOf("く", "け", "こ", "か"),
            "Hiragana của 'ke' là gì?" to listOf("け", "か", "く", "こ"),
            "Hiragana của 'ko' là gì?" to listOf("こ", "け", "か", "く"),
            "Hiragana của 'sa' là gì?" to listOf("さ", "し", "す", "せ"),
            "Hiragana của 'shi' là gì?" to listOf("し", "す", "せ", "そ"),
            "Hiragana của 'su' là gì?" to listOf("す", "せ", "そ", "さ"),
            "Hiragana của 'se' là gì?" to listOf("せ", "そ", "さ", "し"),
            "Hiragana của 'so' là gì?" to listOf("そ", "さ", "し", "す"),
            "Hiragana của 'ta' là gì?" to listOf("た", "ち", "つ", "て"),
            "Hiragana của 'chi' là gì?" to listOf("ち", "つ", "て", "と"),
            "Hiragana của 'tsu' là gì?" to listOf("つ", "て", "と", "た"),
            "Hiragana của 'te' là gì?" to listOf("て", "と", "た", "ち"),
            "Hiragana của 'to' là gì?" to listOf("と", "た", "ち", "つ"),
            "Hiragana của 'na' là gì?" to listOf("な", "に", "ぬ", "ね"),
            "Hiragana của 'ni' là gì?" to listOf("に", "ぬ", "ね", "の"),
            "Hiragana của 'nu' là gì?" to listOf("ぬ", "ね", "の", "な"),
            "Hiragana của 'ne' là gì?" to listOf("ね", "の", "な", "に"),
            "Hiragana của 'no' là gì?" to listOf("の", "な", "に", "ぬ")
        )

        val quizId = "1" // Hiragana
        val setNumber = 1 // Chỉ tạo 1 set duy nhất
        
        // Lấy 10 câu hỏi random từ danh sách
        sampleQuestions.shuffled().take(10).forEachIndexed { index, (text, options) ->
            val question = Question(
                id = 0,
                question = text,
                options = options,
                correctAnswer = 0
            )
            dbHelper.addQuestion(question, quizId, setNumber)
        }
    }
}