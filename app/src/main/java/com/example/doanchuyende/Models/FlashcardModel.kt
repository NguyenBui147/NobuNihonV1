package com.example.doanchuyende.Models

data class FlashcardModel(
    val id: Int,
    val japanese: String,
    val vietnamese: String,
    val romaji: String,
    val category: String = "Basic",
    val difficulty: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun createSampleData(): List<FlashcardModel> {
            return listOf(
                FlashcardModel(1, "こんにちは", "Xin chào", "Konnichiwa", "Lời chào hỏi", 1),
                FlashcardModel(2, "ありがとう", "Cảm ơn", "Arigatou", "Lời chào hỏi", 1),
                FlashcardModel(3, "おはよう", "Chào buổi sáng", "Ohayou", "Lời chào hỏi", 1),
                FlashcardModel(4, "さようなら", "Tạm biệt", "Sayounara", "Lời chào hỏi", 1),
                FlashcardModel(5, "おやすみ", "Chúc ngủ ngon", "Oyasumi", "Lời chào hỏi", 1),
                FlashcardModel(6, "いただきます", "Mời ăn", "Itadakimasu", "Đồ ăn", 2),
                FlashcardModel(7, "ごちそうさま", "Cảm ơn vì bữa ăn", "Gochisousama", "Đồ ăn", 2),
                FlashcardModel(8, "すみません", "Xin lỗi", "Sumimasen", "Lời lịch sự ", 1),
                FlashcardModel(9, "おねがいします", "Làm ơn", "Onegaishimasu", "Lời lịch sự", 2),
                FlashcardModel(10, "がんばって", "Cố lên", "Ganbatte", "Lời động viên", 2),
                FlashcardModel(11, "おめでとう", "Chúc mừng", "Omedetou", "Ăn mừng", 2),
                FlashcardModel(12, "おつかれさま", "Cảm ơn vì công việc", "Otsukaresama", "Chuyên ngành công việc", 2),
                FlashcardModel(13, "ただいま", "Tôi đã về", "Tadaima", "Gia đình và nhà cửa", 1),
                FlashcardModel(14, "おかえり", "Chào mừng về", "Okaeri", "Gia đình và nhà cửa", 1),
                FlashcardModel(15, "いってきます", "Tôi đi đây", "Ittekimasu", "Gia đình và nhà cửa", 2)
            )
        }
    }
} 