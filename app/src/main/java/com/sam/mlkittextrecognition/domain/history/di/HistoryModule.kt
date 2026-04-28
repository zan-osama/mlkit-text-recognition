package com.sam.mlkittextrecognition.domain.history.di

import com.sam.mlkittextrecognition.data.repository.CaptureHistoryRepositoryImpl
import com.sam.mlkittextrecognition.domain.history.CaptureHistoryRepository
import com.sam.mlkittextrecognition.domain.history.DeleteAllCapturesUseCase
import com.sam.mlkittextrecognition.domain.history.DeleteAllCapturesUseCaseImpl
import com.sam.mlkittextrecognition.domain.history.DeleteCaptureUseCase
import com.sam.mlkittextrecognition.domain.history.DeleteCaptureUseCaseImpl
import com.sam.mlkittextrecognition.domain.history.GetAllCapturesUseCase
import com.sam.mlkittextrecognition.domain.history.GetAllCapturesUseCaseImpl
import com.sam.mlkittextrecognition.domain.history.SaveCaptureUseCase
import com.sam.mlkittextrecognition.domain.history.SaveCaptureUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryModule {

    @Binds
    @Singleton
    abstract fun bindCaptureHistoryRepository(
        impl: CaptureHistoryRepositoryImpl
    ): CaptureHistoryRepository

    @Binds
    @Singleton
    abstract fun bindSaveCaptureUseCase(
        impl: SaveCaptureUseCaseImpl
    ): SaveCaptureUseCase

    @Binds
    @Singleton
    abstract fun bindGetAllCapturesUseCase(
        impl: GetAllCapturesUseCaseImpl
    ): GetAllCapturesUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteCaptureUseCase(
        impl: DeleteCaptureUseCaseImpl
    ): DeleteCaptureUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteAllCapturesUseCase(
        impl: DeleteAllCapturesUseCaseImpl
    ): DeleteAllCapturesUseCase
}
