package com.example.doanchuyende.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.doanchuyende.Models.FlashcardModel
import com.example.doanchuyende.Models.CategoryModel

class FlashcardDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "flashcards.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_FLASHCARDS = "flashcards"
        private const val COLUMN_ID = "id"
        private const val COLUMN_JAPANESE = "japanese"
        private const val COLUMN_VIETNAMESE = "vietnamese"
        private const val COLUMN_ROMAJI = "romaji"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_DIFFICULTY = "difficulty"

        private const val TABLE_CATEGORIES = "categories"
        private const val COLUMN_CATEGORY_ID = "id"
        private const val COLUMN_CATEGORY_NAME = "name"
        private const val COLUMN_CATEGORY_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createFlashcardsTable = """
            CREATE TABLE $TABLE_FLASHCARDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_JAPANESE TEXT NOT NULL,
                $COLUMN_VIETNAMESE TEXT NOT NULL,
                $COLUMN_ROMAJI TEXT NOT NULL,
                $COLUMN_CATEGORY TEXT NOT NULL,
                $COLUMN_DIFFICULTY INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createFlashcardsTable)

        val createCategoriesTable = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL UNIQUE,
                $COLUMN_CATEGORY_DESCRIPTION TEXT
            )
        """.trimIndent()
        db.execSQL(createCategoriesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FLASHCARDS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }

    // Flashcard CRUD
    fun addFlashcard(flashcard: FlashcardModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_JAPANESE, flashcard.japanese)
            put(COLUMN_VIETNAMESE, flashcard.vietnamese)
            put(COLUMN_ROMAJI, flashcard.romaji)
            put(COLUMN_CATEGORY, flashcard.category)
            put(COLUMN_DIFFICULTY, flashcard.difficulty)
        }
        val id = db.insert(TABLE_FLASHCARDS, null, values)
        db.close()
        return id
    }

    fun getAllFlashcards(): List<FlashcardModel> {
        val flashcards = mutableListOf<FlashcardModel>()
        val db = readableDatabase
        val cursor = db.query(TABLE_FLASHCARDS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                flashcards.add(
                    FlashcardModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        japanese = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JAPANESE)),
                        vietnamese = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIETNAMESE)),
                        romaji = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROMAJI)),
                        category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return flashcards
    }

    fun getFlashcardsByCategory(category: String): List<FlashcardModel> {
        val flashcards = mutableListOf<FlashcardModel>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FLASHCARDS, null, "$COLUMN_CATEGORY = ?",
            arrayOf(category), null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                flashcards.add(
                    FlashcardModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        japanese = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JAPANESE)),
                        vietnamese = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIETNAMESE)),
                        romaji = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROMAJI)),
                        category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return flashcards
    }

    fun getFlashcardsByDifficulty(difficulty: Int): List<FlashcardModel> {
        val flashcards = mutableListOf<FlashcardModel>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FLASHCARDS, null, "$COLUMN_DIFFICULTY = ?",
            arrayOf(difficulty.toString()), null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                flashcards.add(
                    FlashcardModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        japanese = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_JAPANESE)),
                        vietnamese = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIETNAMESE)),
                        romaji = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROMAJI)),
                        category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        difficulty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return flashcards
    }

    fun deleteFlashcard(id: Int): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_FLASHCARDS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }

    // Category CRUD
    fun addCategory(category: CategoryModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, category.name)
            put(COLUMN_CATEGORY_DESCRIPTION, category.description)
        }
        val id = db.insert(TABLE_CATEGORIES, null, values)
        db.close()
        return id
    }

    fun getAllCategories(): List<CategoryModel> {
        val categories = mutableListOf<CategoryModel>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                categories.add(
                    CategoryModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_DESCRIPTION)) ?: ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return categories
    }

    fun isCategoryExists(name: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CATEGORIES, arrayOf(COLUMN_CATEGORY_ID), "$COLUMN_CATEGORY_NAME = ?",
            arrayOf(name), null, null, null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    fun deleteCategory(id: Int): Int {
        val db = writableDatabase
        val rows = db.delete(TABLE_CATEGORIES, "$COLUMN_CATEGORY_ID = ?", arrayOf(id.toString()))
        db.close()
        return rows
    }
} 