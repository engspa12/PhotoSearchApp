package com.example.dbm.photosearchapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrPhotosResponse(
    @Json(name = "photos")
    var photosGroupNetwork : PhotosGroupNetwork = PhotosGroupNetwork(),
    @Json(name = "stat")
    var stat   : String? = null
)