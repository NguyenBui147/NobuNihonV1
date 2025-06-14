package com.example.doanchuyende.Models

data class AlphabetModel(
    val id: Int,
    val character: String,      // Ký tự Hiragana/Katakana
    val romaji: String,         // Cách đọc romaji
    val type: AlphabetType,     // Loại bảng chữ cái
    val row: String,            // Hàng (a, i, u, e, o)
    val column: String,         // Cột (a, k, s, t, n, h, m, y, r, w)
    val example: String = ""    // Ví dụ sử dụng
)

enum class AlphabetType {
    HIRAGANA,
    KATAKANA
} 