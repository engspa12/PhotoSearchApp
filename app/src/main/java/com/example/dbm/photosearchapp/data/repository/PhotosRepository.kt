package com.example.dbm.photosearchapp.data.repository

import com.example.dbm.photosearchapp.data.datasource.RemoteAPI
import com.example.dbm.photosearchapp.data.model.PhotoNetwork
import com.example.dbm.photosearchapp.di.DispatchersModule
import com.example.dbm.photosearchapp.domain.mapper.toPhotoDomain
import com.example.dbm.photosearchapp.domain.model.PhotoDomain
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import com.example.dbm.photosearchapp.util.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class PhotosRepository @Inject constructor(
    private val networkDataSource: RemoteAPI,
    @DispatchersModule.IODispatcher private val coroutineDispatcher: CoroutineDispatcher
): IPhotosRepository {

    override suspend fun getPhotosFromFeed(): ResultWrapper<List<PhotoDomain>> {
        return withContext(coroutineDispatcher) {
            try {
                val networkResponse = networkDataSource.getPhotosFromFeed()
                val listPhotosDomain = convertToPhotosDomain(networkResponse.photosGroupNetwork.photos)
                ResultWrapper.Success(listPhotosDomain)
            } catch (e: Exception) {
                ResultWrapper.Failure(exception = e)
            }
        }
    }

    override suspend fun getPhotosBySearchTerm(searchTerm: String): ResultWrapper<List<PhotoDomain>> {
       return withContext(coroutineDispatcher) {
           try {
               val networkResponse = networkDataSource.getPhotosBySearchTerm(searchTerm = searchTerm)
               val listPhotosDomain = convertToPhotosDomain(networkResponse.photosGroupNetwork.photos)
               ResultWrapper.Success(listPhotosDomain)
           } catch (e: Exception){
               ResultWrapper.Failure(exception = e)
           }
       }
    }

    private fun convertToPhotosDomain(photosNetwork: List<PhotoNetwork>): List<PhotoDomain> {
        val photosDomain = photosNetwork.mapIndexed { index, photoNetwork ->
            photoNetwork.toPhotoDomain(index)
        }
        return photosDomain
    }

}