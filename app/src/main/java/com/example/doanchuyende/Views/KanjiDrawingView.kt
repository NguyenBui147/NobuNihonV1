package com.example.doanchuyende.Views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class KanjiDrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 40f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val path = Path()
    private val paths = mutableListOf<Path>()
    private var currentX = 0f
    private var currentY = 0f
    
    // Reference kanji properties
    private var referenceKanji: String = ""
    private val referencePaint = Paint().apply {
        color = Color.LTGRAY
        textSize = 600f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        alpha = 100 // Semi-transparent
    }

    fun setReferenceKanji(kanji: String) {
        referenceKanji = kanji
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.reset()
                path.moveTo(x, y)
                currentX = x
                currentY = y
            }
            MotionEvent.ACTION_MOVE -> {
                path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
                currentX = x
                currentY = y
            }
            MotionEvent.ACTION_UP -> {
                path.lineTo(currentX, currentY)
                paths.add(Path(path))
                path.reset()
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val width = width.toFloat()
        val height = height.toFloat()
        
        // Draw grid lines
        val gridPaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 8f
            style = Paint.Style.STROKE
        }
        
        // Vertical lines
        canvas.drawLine(width / 3, 0f, width / 3, height, gridPaint)
        canvas.drawLine(2 * width / 3, 0f, 2 * width / 3, height, gridPaint)
        
        // Horizontal lines
        canvas.drawLine(0f, height / 3, width, height / 3, gridPaint)
        canvas.drawLine(0f, 2 * height / 3, width, 2 * height / 3, gridPaint)
        
        // Draw reference kanji in the center
        if (referenceKanji.isNotEmpty()) {
            canvas.drawText(referenceKanji, width / 2, height / 2 + 60f, referencePaint)
        }
        
        // Draw all paths
        for (p in paths) {
            canvas.drawPath(p, paint)
        }
        
        // Draw current path
        canvas.drawPath(path, paint)
    }

    fun clear() {
        paths.clear()
        path.reset()
        invalidate()
    }
} 