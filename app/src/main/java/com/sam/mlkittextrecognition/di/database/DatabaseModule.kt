package com.sam.mlkittextrecognition.di.database

import android.content.Context
import androidx.room.Room
import com.sam.mlkittextrecognition.data.local.CaptureHistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CaptureHistoryDatabase {
        return Room.databaseBuilder(
            context,
            CaptureHistoryDatabase::class.java,
            "capture_history.db"
        ).build()
    }

    @Provides
    fun provideCaptureHistoryDao(database: CaptureHistoryDatabase): com.sam.mlkittextrecognition.data.local.dao.CaptureHistoryDao {
        return database.captureHistoryDao()
    }
}
