package com.example.dbm.photosearchapp.presentation.state

import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.util.MessageWrapper

data class PhotosUIState(
    val shownIndex: Int = 0,
    val validShownIndex: Boolean = false,
    val errorPresent: Boolean = false,
    val messageWrapper: MessageWrapper = MessageWrapper(),
    val listPhotos: List<PhotoView> = listOf(),
    val listHasChanged: Boolean = false,
    val isLoading: Boolean = false
)

val PhotosUIState.listIsFull: Boolean
    get() = listPhotos.isNotEmpty()