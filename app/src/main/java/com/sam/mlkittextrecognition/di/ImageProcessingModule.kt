package com.sam.mlkittextrecognition.di

import com.sam.mlkittextrecognition.data.preprocessing.ImagePreProcessorImpl
import com.sam.mlkittextrecognition.domain.model.ImageProcessingConfig
import com.sam.mlkittextrecognition.domain.preprocessing.ImagePreProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageProcessingModule {

    @Provides
    @Singleton
    fun provideImageProcessingConfig(): ImageProcessingConfig {
        return ImageProcessingConfig(
            enhanceContrast = true,
            contrastLevel = 1.5f,
            removeLightBlueBackground = true
        )
    }

    @Provides
    @Singleton
    fun bindImagePreProcessor(
        impl: ImagePreProcessorImpl
    ): ImagePreProcessor {
        return impl
    }
}
