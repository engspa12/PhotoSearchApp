package com.example.dbm.photosearchapp.presentation.state

import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.presentation.util.PhotosViewError
import com.example.dbm.photosearchapp.presentation.util.TitleType

data class PhotosUIState(
    val shownIndex: Int = 0,
    val validShownIndex: Boolean = false,
    val errorPresent: Boolean = false,
    val errorType: PhotosViewError = PhotosViewError.NONE,
    val titleType: TitleType = TitleType.NONE,
    val listPhotos: List<PhotoView> = listOf(),
    val listHasChanged: Boolean = false,
    val isLoading: Boolean = false
)

val PhotosUIState.listIsEmpty: Boolean
    get() = listPhotos.isEmpty()