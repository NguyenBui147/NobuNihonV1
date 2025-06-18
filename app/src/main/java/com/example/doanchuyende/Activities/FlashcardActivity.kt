package com.example.doanchuyende.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.doanchuyende.Database.FlashcardDatabaseHelper
import com.example.doanchuyende.Models.CategoryModel
import com.example.doanchuyende.Models.FlashcardModel
import com.example.doanchuyende.R
import com.google.android.material.textfield.TextInputEditText

class FlashcardActivity : AppCompatActivity() {
    
    private lateinit var cardView: CardView
    private lateinit var frontText: TextView
    private lateinit var backText: TextView
    private lateinit var cardNumberText: TextView
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var flipButton: ImageButton
    private lateinit var addButton: ImageButton
    private lateinit var filterButton: ImageButton
    private lateinit var shuffleButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var categoryButton: ImageButton
    
    private var isFlipped = false
    private var currentCardIndex = 0
    private var flashcards = mutableListOf<FlashcardModel>()
    private var filteredFlashcards = mutableListOf<FlashcardModel>()
    private lateinit var databaseHelper: FlashcardDatabaseHelper
    private var isAnimating = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)
        
        databaseHelper = FlashcardDatabaseHelper(this)
        initializeData()
        initializeViews()
        setupClickListeners()
        showCurrentCard()
    }
    
    private fun initializeData() {
        // Load data from database
        flashcards = databaseHelper.getAllFlashcards().toMutableList()
        filteredFlashcards = flashcards.toMutableList()
    }
    
    private fun initializeViews() {
        cardView = findViewById(R.id.cardView)
        frontText = findViewById(R.id.frontText)
        backText = findViewById(R.id.backText)
        cardNumberText = findViewById(R.id.cardNumberText)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        flipButton = findViewById(R.id.flipButton)
        addButton = findViewById(R.id.addButton)
        filterButton = findViewById(R.id.filterButton)
        shuffleButton = findViewById(R.id.shuffleButton)
        deleteButton = findViewById(R.id.deleteButton)
        categoryButton = findViewById(R.id.categoryButton)
        
        // Setup gesture detector for swipe
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val diffX = e2.x - (e1?.x ?: 0f)
                val diffY = e2.y - (e1?.y ?: 0f)
                
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                        if (diffX > 0) {
                            // Swipe right - previous card
                            showPreviousCard()
                        } else {
                            // Swipe left - next card
                            showNextCard()
                        }
                        return true
                    }
                }
                return false
            }
        })
        
        cardView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
        
        // Add click listener for card flip
        cardView.setOnClickListener {
            flipCard()
        }
    }
    
    private fun setupClickListeners() {
        prevButton.setOnClickListener {
            showPreviousCard()
        }
        
        nextButton.setOnClickListener {
            showNextCard()
        }
        
        flipButton.setOnClickListener {
            flipCard()
        }
        
        addButton.setOnClickListener {
            showAddFlashcardDialog()
        }
        
        filterButton.setOnClickListener {
            showFilterDialog()
        }
        
        shuffleButton.setOnClickListener {
            shuffleFlashcards()
        }
        
        deleteButton.setOnClickListener {
            showDeleteConfirmDialog()
        }
        
        categoryButton.setOnClickListener {
            showCategoryManagementDialog()
        }
    }
    
    private fun flipCard() {
        if (isAnimating) return
        
        isAnimating = true
        
        // Set camera distance for 3D effect
        cardView.cameraDistance = 8000f
        
        // First half of the flip animation (rotate to 90 degrees)
        val firstHalfFlip = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 90f)
        firstHalfFlip.duration = 200
        
        firstHalfFlip.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Switch the content
                isFlipped = !isFlipped
                updateCardDisplay()
                
                // Second half of the flip animation (rotate from 90 to 180 degrees)
                val secondHalfFlip = ObjectAnimator.ofFloat(cardView, "rotationY", 90f, 180f)
                secondHalfFlip.duration = 200
                
                secondHalfFlip.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // Reset rotation to 0 for next flip
                        cardView.rotationY = 0f
                        isAnimating = false
                    }
                })
                
                secondHalfFlip.start()
            }
        })
        
        firstHalfFlip.start()
    }
    
    private fun showCategoryManagementDialog() {
        val options = arrayOf("Thêm Danh mục Kanji mới", "Xem tất cả danh mục")
        
        AlertDialog.Builder(this)
            .setTitle("Quản lý Danh mục Kanji")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showAddCategoryDialog()
                    1 -> showAllCategoriesDialog()
                }
            }
            .show()
    }
    
    private fun showAddCategoryDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        
        val etCategoryName = dialogView.findViewById<TextInputEditText>(R.id.etCategoryName)
        val etCategoryDescription = dialogView.findViewById<TextInputEditText>(R.id.etCategoryDescription)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)
        
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        btnAdd.setOnClickListener {
            val categoryName = etCategoryName.text.toString().trim()
            val categoryDescription = etCategoryDescription.text.toString().trim()
            
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (databaseHelper.isCategoryExists(categoryName)) {
                Toast.makeText(this, "Danh mục này đã tồn tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val newCategory = CategoryModel(
                id = 0,
                name = categoryName,
                description = categoryDescription
            )
            
            val newId = databaseHelper.addCategory(newCategory)
            if (newId > 0) {
                Toast.makeText(this, "Đã thêm danh mục mới: $categoryName", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialog.show()
    }
    
    private fun showAllCategoriesDialog() {
        val categories = databaseHelper.getAllCategories()
        if (categories.isEmpty()) {
            Toast.makeText(this, "Không có danh mục nào", Toast.LENGTH_SHORT).show()
            return
        }
        
        val categoryNames = categories.map { "${it.name}${if (it.description.isNotEmpty()) " - ${it.description}" else ""}" }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Tất cả Danh mục")
            .setItems(categoryNames) { _, which ->
                val selectedCategory = categories[which]
                showCategoryOptionsDialog(selectedCategory)
            }
            .setPositiveButton("Đóng", null)
            .show()
    }
    
    private fun showCategoryOptionsDialog(category: CategoryModel) {
        val options = arrayOf("Xem flashcard trong danh mục", "Xóa danh mục")
        
        AlertDialog.Builder(this)
            .setTitle("Danh mục: ${category.name}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        filteredFlashcards = databaseHelper.getFlashcardsByCategory(category.name).toMutableList()
                        currentCardIndex = 0
                        showCurrentCard()
                    }
                    1 -> showDeleteCategoryConfirmDialog(category)
                }
            }
            .show()
    }
    
    private fun showDeleteCategoryConfirmDialog(category: CategoryModel) {
        val flashcardsInCategory = databaseHelper.getFlashcardsByCategory(category.name)
        
        AlertDialog.Builder(this)
            .setTitle("Xóa Danh mục")
            .setMessage("Bạn có chắc muốn xóa danh mục '${category.name}'?\n\nDanh mục này có ${flashcardsInCategory.size} flashcard. Tất cả flashcard sẽ bị xóa.")
            .setPositiveButton("Xóa") { _, _ ->
                deleteCategory(category)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun deleteCategory(category: CategoryModel) {
        // Delete all flashcards in this category first
        val flashcardsInCategory = databaseHelper.getFlashcardsByCategory(category.name)
        for (flashcard in flashcardsInCategory) {
            databaseHelper.deleteFlashcard(flashcard.id)
        }
        
        // Delete the category
        val deletedRows = databaseHelper.deleteCategory(category.id)
        
        if (deletedRows > 0) {
            // Reload data
            initializeData()
            currentCardIndex = 0
            showCurrentCard()
            Toast.makeText(this, "Đã xóa danh mục: ${category.name}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Lỗi khi xóa danh mục", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shuffleFlashcards() {
        if (filteredFlashcards.isNotEmpty()) {
            filteredFlashcards.shuffle()
            currentCardIndex = 0
            showCurrentCard()
            Toast.makeText(this, "Đã xáo trộn flashcard", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showDeleteConfirmDialog() {
        if (filteredFlashcards.isEmpty()) {
            Toast.makeText(this, "Không có flashcard để xóa", Toast.LENGTH_SHORT).show()
            return
        }
        
        val currentCard = filteredFlashcards[currentCardIndex]
        AlertDialog.Builder(this)
            .setTitle("Xóa Flashcard")
            .setMessage("Bạn có chắc muốn xóa flashcard này?\n\n${currentCard.japanese}\n${currentCard.vietnamese}")
            .setPositiveButton("Xóa") { _, _ ->
                deleteCurrentFlashcard()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
    
    private fun deleteCurrentFlashcard() {
        val currentCard = filteredFlashcards[currentCardIndex]
        val deletedRows = databaseHelper.deleteFlashcard(currentCard.id)
        
        if (deletedRows > 0) {
            // Remove from both lists
            flashcards.removeAll { it.id == currentCard.id }
            filteredFlashcards.removeAt(currentCardIndex)
            
            // Adjust current index
            if (filteredFlashcards.isEmpty()) {
                currentCardIndex = 0
                showCurrentCard()
                Toast.makeText(this, "Đã xóa flashcard", Toast.LENGTH_SHORT).show()
            } else {
                if (currentCardIndex >= filteredFlashcards.size) {
                    currentCardIndex = filteredFlashcards.size - 1
                }
                showCurrentCard()
                Toast.makeText(this, "Đã xóa flashcard", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Lỗi khi xóa flashcard", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showAddFlashcardDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_flashcard, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        
        val etJapanese = dialogView.findViewById<TextInputEditText>(R.id.etJapanese)
        val etVietnamese = dialogView.findViewById<TextInputEditText>(R.id.etVietnamese)
        val etRomaji = dialogView.findViewById<TextInputEditText>(R.id.etRomaji)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)
        val radioGroupDifficulty = dialogView.findViewById<RadioGroup>(R.id.radioGroupDifficulty)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)
        
        // Setup category spinner with existing categories from database
        val categories = databaseHelper.getAllCategories().map { it.name }.toMutableList()
        if (!categories.contains("Custom")) {
            categories.add("Custom")
        }
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
        
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        btnAdd.setOnClickListener {
            val japanese = etJapanese.text.toString().trim()
            val vietnamese = etVietnamese.text.toString().trim()
            val romaji = etRomaji.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()
            
            if (japanese.isEmpty() || vietnamese.isEmpty() || romaji.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val difficulty = when (radioGroupDifficulty.checkedRadioButtonId) {
                R.id.rbEasy -> 1
                R.id.rbMedium -> 2
                R.id.rbHard -> 3
                else -> 1
            }
            
            val newFlashcard = FlashcardModel(
                id = 0, // Will be set by database
                japanese = japanese,
                vietnamese = vietnamese,
                romaji = romaji,
                category = category,
                difficulty = difficulty
            )
            
            val newId = databaseHelper.addFlashcard(newFlashcard)
            if (newId > 0) {
                // Reload data from database
                initializeData()
                currentCardIndex = filteredFlashcards.size - 1
                showCurrentCard()
                Toast.makeText(this, "Đã thêm flashcard mới", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Lỗi khi thêm flashcard", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun showFilterDialog() {
        val categories = databaseHelper.getAllCategories().map { it.name }
        val difficulties = listOf("Tất cả", "Dễ", "Trung bình", "Khó")
        
        val filterOptions = arrayOf("Theo danh mục", "Theo độ khó", "Hiển thị tất cả")
        
        AlertDialog.Builder(this)
            .setTitle("Lọc Flashcard")
            .setItems(filterOptions) { _, which ->
                when (which) {
                    0 -> showCategoryFilterDialog(categories)
                    1 -> showDifficultyFilterDialog(difficulties)
                    2 -> {
                        filteredFlashcards = flashcards.toMutableList()
                        currentCardIndex = 0
                        showCurrentCard()
                    }
                }
            }
            .show()
    }
    
    private fun showCategoryFilterDialog(categories: List<String>) {
        val categoryArray = categories.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Chọn danh mục")
            .setItems(categoryArray) { _, which ->
                val selectedCategory = categoryArray[which]
                filteredFlashcards = databaseHelper.getFlashcardsByCategory(selectedCategory).toMutableList()
                currentCardIndex = 0
                showCurrentCard()
            }
            .show()
    }
    
    private fun showDifficultyFilterDialog(difficulties: List<String>) {
        val difficultyArray = difficulties.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle("Chọn độ khó")
            .setItems(difficultyArray) { _, which ->
                when (which) {
                    0 -> {
                        filteredFlashcards = flashcards.toMutableList()
                    }
                    1 -> {
                        filteredFlashcards = databaseHelper.getFlashcardsByDifficulty(1).toMutableList()
                    }
                    2 -> {
                        filteredFlashcards = databaseHelper.getFlashcardsByDifficulty(2).toMutableList()
                    }
                    3 -> {
                        filteredFlashcards = databaseHelper.getFlashcardsByDifficulty(3).toMutableList()
                    }
                }
                currentCardIndex = 0
                showCurrentCard()
            }
            .show()
    }
    
    private fun showCurrentCard() {
        if (filteredFlashcards.isNotEmpty()) {
            val currentCard = filteredFlashcards[currentCardIndex]
            frontText.text = currentCard.japanese
            backText.text = "${currentCard.vietnamese}\n${currentCard.romaji}\n\nDanh mục: ${currentCard.category}\nĐộ khó: ${getDifficultyText(currentCard.difficulty)}"
            cardNumberText.text = "${currentCardIndex + 1}/${filteredFlashcards.size}"
            
            // Reset card to front and reset rotation
            isFlipped = false
            cardView.rotationY = 0f
            updateCardDisplay()
            
            // Add a subtle bounce animation when changing cards
            addCardChangeAnimation()
        } else {
            frontText.text = "Không có flashcard nào"
            backText.text = ""
            cardNumberText.text = "0/0"
        }
    }
    
    private fun addCardChangeAnimation() {
        // Scale down slightly
        val scaleDown = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 0.95f)
        scaleDown.duration = 100
        
        // Scale back up with bounce
        val scaleUp = ObjectAnimator.ofFloat(cardView, "scaleX", 0.95f, 1.05f, 1f)
        scaleUp.duration = 200
        
        scaleDown.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                scaleUp.start()
            }
        })
        
        scaleDown.start()
    }
    
    private fun getDifficultyText(difficulty: Int): String {
        return when (difficulty) {
            1 -> "Dễ"
            2 -> "Trung bình"
            3 -> "Khó"
            else -> "Dễ"
        }
    }
    
    private fun showPreviousCard() {
        if (currentCardIndex > 0) {
            currentCardIndex--
            showCurrentCard()
        }
    }
    
    private fun showNextCard() {
        if (currentCardIndex < filteredFlashcards.size - 1) {
            currentCardIndex++
            showCurrentCard()
        }
    }
    
    private fun updateCardDisplay() {
        if (isFlipped) {
            frontText.visibility = View.GONE
            backText.visibility = View.VISIBLE
        } else {
            frontText.visibility = View.VISIBLE
            backText.visibility = View.GONE
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
} 