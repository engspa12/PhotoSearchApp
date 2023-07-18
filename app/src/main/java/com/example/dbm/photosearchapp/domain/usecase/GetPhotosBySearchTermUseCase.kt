package com.example.dbm.photosearchapp.domain.usecase

import com.example.dbm.photosearchapp.domain.mapper.toPhotoView
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import com.example.dbm.photosearchapp.domain.util.PhotosDomainError
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.util.ResultWrapper
import java.io.IOException
import javax.inject.Inject

interface IGetPhotosBySearchTermUseCase {
    suspend operator fun invoke(searchTerm: String): ResultWrapper<List<PhotoView>, PhotosDomainError>
}

class GetPhotosBySearchTermUseCase @Inject constructor(
    private val photosRepository: IPhotosRepository
): IGetPhotosBySearchTermUseCase {

    override suspend fun invoke(searchTerm: String): ResultWrapper<List<PhotoView>, PhotosDomainError> {

        return when(val result = photosRepository.getPhotosBySearchTerm(searchTerm)){
            is ResultWrapper.Success -> {
                val listPhotosView = result.value.map {
                    it.toPhotoView()
                }
                ResultWrapper.Success(listPhotosView)
            }
            is ResultWrapper.Failure -> {
                if(result.exception is IOException){
                    ResultWrapper.Failure(error = PhotosDomainError.GENERIC_ERROR)
                } else {
                    ResultWrapper.Failure(error = PhotosDomainError.UNKNOWN_ERROR)
                }
            }
        }
    }
}