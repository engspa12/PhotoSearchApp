package com.example.dbm.photosearchapp.domain.mapper

import com.example.dbm.photosearchapp.data.model.PhotoNetwork
import com.example.dbm.photosearchapp.domain.model.PhotoDomain
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.util.formatDate
import com.example.dbm.photosearchapp.util.getImageUrl

fun PhotoNetwork.toPhotoDomain(id: Int) = PhotoDomain(
    id = id,
    title = if (this.title != "") this.title ?: "No title available" else "No title available",
    date = this.datetaken?.formatDate() ?: "No date available",
    author = if(this.ownername != "") this.ownername ?: "No author available" else "No author available",
    imgUrl = this.getImageUrl()
)

fun PhotoDomain.toPhotoView() = PhotoView(
    id = this.id,
    title = this.title,
    date = this.date,
    author = this.author,
    imgUrl = this.imgUrl
)
