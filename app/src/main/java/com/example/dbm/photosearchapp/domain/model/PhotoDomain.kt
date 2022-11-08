package com.example.dbm.photosearchapp.domain.model

data class PhotoDomain(
    val id: Int,
    val title: String,
    val date: String,
    val author: String,
    val imgUrl: String
)