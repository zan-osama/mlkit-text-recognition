package com.sam.mlkittextrecognition.domain.usecase.di

import com.sam.mlkittextrecognition.domain.camera.CameraUseCase
import com.sam.mlkittextrecognition.domain.camera.CameraUseCaseImpl
import com.sam.mlkittextrecognition.domain.usecase.RecognizeTextUseCase
import com.sam.mlkittextrecognition.domain.usecase.RecognizeTextUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindRecognizeTextUseCase(
        useCase: RecognizeTextUseCaseImpl
    ): RecognizeTextUseCase

    @Binds
    @Singleton
    abstract fun bindCameraUseCase(
        useCase: CameraUseCaseImpl
    ): CameraUseCase
}