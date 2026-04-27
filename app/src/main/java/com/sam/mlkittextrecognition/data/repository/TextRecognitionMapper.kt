package com.sam.mlkittextrecognition.data.repository

import com.google.mlkit.vision.text.Text
import com.sam.mlkittextrecognition.domain.model.BoundingBox
import com.sam.mlkittextrecognition.domain.model.LineDomain
import com.sam.mlkittextrecognition.domain.model.TextBlockDomain
import com.sam.mlkittextrecognition.domain.model.TextDomain

object TextRecognitionMapper {

    fun map(visionText: Text): TextDomain {
        return TextDomain(
            text = visionText.text,
            textBlocks = visionText.textBlocks.map { block ->
                TextBlockDomain(
                    text = block.text,
                    boundingBox = block.boundingBox?.let {
                        BoundingBox(it.left, it.top, it.right, it.bottom)
                    },
                    lines = block.lines.map { line ->
                        LineDomain(
                            text = line.text,
                            boundingBox = line.boundingBox?.let {
                                BoundingBox(it.left, it.top, it.right, it.bottom)
                            }
                        )
                    }
                )
            }
        )
    }
}