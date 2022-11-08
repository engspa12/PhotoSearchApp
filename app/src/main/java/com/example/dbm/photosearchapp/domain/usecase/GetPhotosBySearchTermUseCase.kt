package com.example.dbm.photosearchapp.domain.usecase

import com.example.dbm.photosearchapp.domain.mapper.toPhotoView
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import javax.inject.Inject

interface IGetPhotosBySearchTermUseCase {
    suspend operator fun invoke(searchTerm: String): List<PhotoView>
}

class GetPhotosBySearchTermUseCase @Inject constructor(
    private val photosRepository: IPhotosRepository
): IGetPhotosBySearchTermUseCase {

    override suspend fun invoke(searchTerm: String): List<PhotoView> {
       return photosRepository.getPhotosBySearchTerm(searchTerm).map {
           it.toPhotoView()
       }
    }
}