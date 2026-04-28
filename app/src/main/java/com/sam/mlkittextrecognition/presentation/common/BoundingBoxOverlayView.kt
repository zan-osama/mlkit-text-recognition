package com.sam.mlkittextrecognition.presentation.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.sam.mlkittextrecognition.domain.model.BoundingBox
import com.sam.mlkittextrecognition.domain.model.TextRecognitionData
import com.sam.mlkittextrecognition.util.BoundingBoxUtils
import com.sam.mlkittextrecognition.util.BoundingBoxUtils.DisplayBounds

class BoundingBoxOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textRecognitionData: TextRecognitionData? = null
    private var displayBounds: DisplayBounds? = null
    private var showTextBlock: Boolean = true
    private var showLines: Boolean = true

    private val textBlockPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val linePaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 32f
        isAntiAlias = true
    }

    private val textBackgroundPaint = Paint().apply {
        color = Color.argb(180, 0, 0, 0)
        style = Paint.Style.FILL
    }

    private val textBlockRect = RectF()
    private val lineRect = RectF()

    fun setData(data: TextRecognitionData, displayBounds: DisplayBounds) {
        this.textRecognitionData = data
        this.displayBounds = displayBounds
        invalidate()
    }

    fun setVisibility(showTextBlock: Boolean, showLines: Boolean) {
        this.showTextBlock = showTextBlock
        this.showLines = showLines
        invalidate()
    }

    fun clearData() {
        this.textRecognitionData = null
        this.displayBounds = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val data = textRecognitionData ?: return
        val bounds = displayBounds ?: return

        if (bounds.scaledWidth <= 0 || bounds.scaledHeight <= 0 ||
            data.imageWidth <= 0 || data.imageHeight <= 0) {
            return
        }

        for (textBlock in data.textDomain.textBlocks) {
            if (showTextBlock) {
                drawBoundingBox(canvas, textBlock.boundingBox, textBlockPaint, textBlock.text, bounds, data)
            }

            if (showLines) {
                for (line in textBlock.lines) {
                    drawLineBox(canvas, line.boundingBox, linePaint, bounds, data)
                }
            }
        }
    }

    private fun drawBoundingBox(
        canvas: Canvas,
        boundingBox: BoundingBox?,
        paint: Paint,
        text: String,
        bounds: DisplayBounds,
        data: TextRecognitionData
    ) {
        boundingBox ?: return

        textBlockRect.set(
            BoundingBoxUtils.transformBoundingBox(
                boundingBox,
                bounds,
                data.imageWidth,
                data.imageHeight
            )
        )

//        canvas.drawRect(textBlockRect, paint)
        drawTextLabel(canvas, text, textBlockRect.left, textBlockRect.top, textBackgroundPaint, textPaint)
    }

    private fun drawLineBox(
        canvas: Canvas,
        boundingBox: BoundingBox?,
        paint: Paint,
        bounds: DisplayBounds,
        data: TextRecognitionData
    ) {
        boundingBox ?: return

        lineRect.set(
            BoundingBoxUtils.transformBoundingBox(
                boundingBox,
                bounds,
                data.imageWidth,
                data.imageHeight
            )
        )

        canvas.drawRect(lineRect, paint)
    }

    private fun drawTextLabel(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        bgPaint: Paint,
        txtPaint: Paint
    ) {
        val displayText = if (text.length > 30) text.take(30) + "..." else text
        val textWidth = txtPaint.measureText(displayText)
        val padding = 8f

        canvas.drawRect(
            x - padding,
            y - txtPaint.textSize - padding,
            x + textWidth + padding,
            y + padding,
            bgPaint
        )

        canvas.drawText(
            displayText,
            x,
            y - padding,
            txtPaint
        )
    }
}