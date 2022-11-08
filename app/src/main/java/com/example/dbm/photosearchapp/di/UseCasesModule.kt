package com.example.dbm.photosearchapp.di

import com.example.dbm.photosearchapp.domain.usecase.GetPhotosBySearchTermUseCase
import com.example.dbm.photosearchapp.domain.usecase.GetPhotosFromFeedUseCase
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosBySearchTermUseCase
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosFromFeedUseCase
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetPhotosBySearchTermUseCase(
        photosRepository: IPhotosRepository
    ): IGetPhotosBySearchTermUseCase {
        return GetPhotosBySearchTermUseCase(photosRepository)
    }

    @Provides
    fun provideGetPhotosFromFeedUseCase(
        photosRepository: IPhotosRepository
    ): IGetPhotosFromFeedUseCase {
        return GetPhotosFromFeedUseCase(photosRepository)
    }
}