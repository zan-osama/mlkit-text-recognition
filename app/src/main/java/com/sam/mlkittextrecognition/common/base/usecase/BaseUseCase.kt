package com.sam.mlkittextrecognition.common.base.usecase

interface BaseUseCase<in P, out R> {
     suspend operator fun invoke(parameters: P): R
}