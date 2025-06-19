package com.example.doanchuyende.Models

data class FlashcardModel(
    val id: Int,
    val japanese: String,
    val vietnamese: String,
    val romaji: String,
    val category: String = "Basic",
    val difficulty: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)