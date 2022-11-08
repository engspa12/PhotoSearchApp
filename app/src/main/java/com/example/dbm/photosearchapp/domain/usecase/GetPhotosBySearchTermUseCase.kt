package com.example.dbm.photosearchapp.domain.usecase

import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.domain.mapper.toPhotoView
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.util.MessageWrapper
import com.example.dbm.photosearchapp.util.ResultWrapper
import java.io.IOException
import javax.inject.Inject

interface IGetPhotosBySearchTermUseCase {
    suspend operator fun invoke(searchTerm: String): ResultWrapper<List<PhotoView>>
}

class GetPhotosBySearchTermUseCase @Inject constructor(
    private val photosRepository: IPhotosRepository
): IGetPhotosBySearchTermUseCase {

    override suspend fun invoke(searchTerm: String): ResultWrapper<List<PhotoView>> {

        return when(val result = photosRepository.getPhotosBySearchTerm(searchTerm)){
            is ResultWrapper.Success -> {
                val listPhotosView = result.value.map {
                    it.toPhotoView()
                }
                ResultWrapper.Success(listPhotosView)
            }
            is ResultWrapper.Failure -> {
                if(result.exception is IOException){
                    ResultWrapper.Failure(errorMessage = MessageWrapper(messageResource = R.string.error_message))
                } else {
                    ResultWrapper.Failure(errorMessage = MessageWrapper(messageResource = R.string.error_unknown, argForResource  = result.exception?.message ?: ""))
                }
            }
        }
    }
}