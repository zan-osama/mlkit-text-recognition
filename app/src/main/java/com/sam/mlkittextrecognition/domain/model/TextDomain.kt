package com.sam.mlkittextrecognition.domain.model

data class TextDomain(
    val text: String,
    val textBlocks: List<TextBlockDomain>
)

data class TextBlockDomain(
    val text: String,
    val boundingBox: BoundingBox?,
    val lines: List<LineDomain>
)

data class LineDomain(
    val text: String,
    val boundingBox: BoundingBox?
)

data class BoundingBox(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)

data class TextRecognitionData(
    val textDomain: TextDomain,
    val imageWidth: Int,
    val imageHeight: Int
)