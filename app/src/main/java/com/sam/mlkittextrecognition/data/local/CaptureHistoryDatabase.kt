package com.sam.mlkittextrecognition.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sam.mlkittextrecognition.data.local.dao.CaptureHistoryDao
import com.sam.mlkittextrecognition.data.local.entity.CaptureHistoryEntity
import com.sam.mlkittextrecognition.data.local.entity.DateConverter

@Database(entities = [CaptureHistoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class CaptureHistoryDatabase : RoomDatabase() {
    abstract fun captureHistoryDao(): CaptureHistoryDao
}
