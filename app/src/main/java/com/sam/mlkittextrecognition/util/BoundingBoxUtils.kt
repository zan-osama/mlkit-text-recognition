package com.sam.mlkittextrecognition.util

import android.graphics.RectF
import com.sam.mlkittextrecognition.domain.model.BoundingBox

object BoundingBoxUtils {

    data class DisplayBounds(
        val scaledWidth: Int,
        val scaledHeight: Int,
        val offsetX: Int,
        val offsetY: Int
    )

    fun calculateDisplayBounds(
        imageWidth: Int,
        imageHeight: Int,
        viewWidth: Int,
        viewHeight: Int
    ): DisplayBounds {
        val viewRatio = viewWidth.toFloat() / viewHeight
        val imageRatio = imageWidth.toFloat() / imageHeight

        val (scaledWidth, scaledHeight) = if (imageRatio > viewRatio) {
            Pair(viewWidth, (viewWidth / imageRatio).toInt())
        } else {
            Pair((viewHeight * imageRatio).toInt(), viewHeight)
        }

        val offsetX = (viewWidth - scaledWidth) / 2
        val offsetY = (viewHeight - scaledHeight) / 2

        return DisplayBounds(scaledWidth, scaledHeight, offsetX, offsetY)
    }

    fun transformBoundingBox(
        bbox: BoundingBox,
        bounds: DisplayBounds,
        imageWidth: Int,
        imageHeight: Int
    ): RectF {
        val scaleX = bounds.scaledWidth.toFloat() / imageWidth
        val scaleY = bounds.scaledHeight.toFloat() / imageHeight

        return RectF(
            bounds.offsetX + bbox.left * scaleX,
            bounds.offsetY + bbox.top * scaleY,
            bounds.offsetX + bbox.right * scaleX,
            bounds.offsetY + bbox.bottom * scaleY
        )
    }
}