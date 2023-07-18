package com.example.dbm.photosearchapp.domain.repository

import com.example.dbm.photosearchapp.domain.model.PhotoDomain
import com.example.dbm.photosearchapp.util.ResultWrapper

interface IPhotosRepository {
    suspend fun getPhotosFromFeed(): ResultWrapper<List<PhotoDomain>, Nothing>
    suspend fun getPhotosBySearchTerm(searchTerm: String): ResultWrapper<List<PhotoDomain>, Nothing>
}