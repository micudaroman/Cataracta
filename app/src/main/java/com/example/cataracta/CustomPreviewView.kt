package com.example.cataracta

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat

class CustomPreviewView(context: Context) : View(context) {

        private val previewView = PreviewView(context)

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            previewView.measure(widthMeasureSpec, heightMeasureSpec)
        }

        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            previewView.layout(left, top, right, bottom)
        }

    private val overlayPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        alpha = 128
    }

    private val overlayDrawable: Drawable = ContextCompat.getDrawable(context, R.drawable.overlay)!!

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the overlay drawable
        overlayDrawable.setBounds(0, 0, width, height/2)
        overlayDrawable.draw(canvas)

        // Draw the overlay paint
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)
    }
}