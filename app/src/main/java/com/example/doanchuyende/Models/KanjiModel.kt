package com.example.doanchuyende.Models

data class KanjiModel(
    val id: String,
    val kanji: String,
    val meaning: String,
    val onyomi: String,
    val kunyomi: String,
    val radical: String,
    val examples: List<String>
) 