package com.example.doanchuyende.Models

data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
) 