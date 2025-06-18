package com.example.doanchuyende.Models

data class CategoryModel(
    val id: Int,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun createDefaultCategories(): List<CategoryModel> {
            return listOf(
                CategoryModel(1, "Từ cơ bản", "Các từ cơ bản"),
                CategoryModel(2, "Lời chào hỏi", "Lời chào hỏi"),
                CategoryModel(3, "Thức ăn và ẩm thực", "Thức ăn và ẩm thực"),
                CategoryModel(4, "Lời lịch sự", "Lịch sự và phép tắc"),
                CategoryModel(5, "Lời động viên", "Khuyến khích và động viên"),
                CategoryModel(6, "Ăn mừng", "Lễ hội và chúc mừng"),
                CategoryModel(7, "Chuyên ngành công việc", "Công việc và văn phòng"),
                CategoryModel(8, "Gia đình và nhà cửa", "Gia đình và nhà cửa")
            )
        }
    }
} 