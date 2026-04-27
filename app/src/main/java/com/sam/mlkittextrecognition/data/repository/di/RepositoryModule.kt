package com.sam.mlkittextrecognition.data.repository.di

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.sam.mlkittextrecognition.data.repository.TextRecognitionRepositoryImpl
import com.sam.mlkittextrecognition.domain.repository.TextRecognitionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTextRecognitionRepository(recognizer: TextRecognizer): TextRecognitionRepository {
        return TextRecognitionRepositoryImpl(recognizer)
    }

    @Provides
    @Singleton
    fun provideTextRecognizer(): TextRecognizer {
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }
}