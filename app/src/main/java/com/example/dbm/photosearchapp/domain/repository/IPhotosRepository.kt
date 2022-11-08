package com.example.dbm.photosearchapp.domain.repository

import com.example.dbm.photosearchapp.domain.model.PhotoDomain

interface IPhotosRepository {
    suspend fun getPhotosFromFeed(): List<PhotoDomain>
    suspend fun getPhotosBySearchTerm(searchTerm: String): List<PhotoDomain>
}