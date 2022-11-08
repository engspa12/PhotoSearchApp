package com.example.dbm.photosearchapp.di

import com.example.dbm.photosearchapp.data.datasource.RemoteAPI
import com.example.dbm.photosearchapp.data.repository.PhotosRepository
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePhotosRepository(
        networkDataSource: RemoteAPI,
        @DispatchersModule.IODispatcher dispatcher: CoroutineDispatcher
    ): IPhotosRepository {
        return PhotosRepository(networkDataSource, dispatcher)
    }
}