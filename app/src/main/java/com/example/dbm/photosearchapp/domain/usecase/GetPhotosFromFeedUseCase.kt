package com.example.dbm.photosearchapp.domain.usecase

import com.example.dbm.photosearchapp.domain.mapper.toPhotoView
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import javax.inject.Inject

interface IGetPhotosFromFeedUseCase {
    suspend operator fun invoke(): List<PhotoView>
}

class GetPhotosFromFeedUseCase @Inject constructor(
    private val photosRepository: IPhotosRepository
): IGetPhotosFromFeedUseCase {

    override suspend fun invoke(): List<PhotoView> {
        return photosRepository.getPhotosFromFeed().map {
            it.toPhotoView()
        }
    }

}