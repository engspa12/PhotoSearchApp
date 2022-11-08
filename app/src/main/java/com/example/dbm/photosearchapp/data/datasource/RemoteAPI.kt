package com.example.dbm.photosearchapp.data.datasource

import com.example.dbm.photosearchapp.data.model.FlickrPhotosResponse
import com.example.dbm.photosearchapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteAPI {

    @GET("rest/?method=flickr.interestingness.getList")
    suspend fun getPhotosFromFeed(
        @Query("api_key") apiKey: String = Constants.API_KEY_VALUE,
        @Query("per_page") perPage: Int = Constants.PER_PAGE_VALUE,
        @Query("format") format: String = Constants.FORMAT_VALUE,
        @Query("extras") extras: String = Constants.EXTRAS_VALUE,
        @Query("nojsoncallback") nojsoncallback: String = Constants.NO_JSON_CALLBACK_VALUE
    ): FlickrPhotosResponse

    @GET("rest/?method=flickr.photos.search")
    suspend fun getPhotosBySearchTerm(
        @Query("api_key") apiKey: String = Constants.API_KEY_VALUE,
        @Query("text") searchTerm: String,
        @Query("sort") sort: String = Constants.SORT_VALUE,
        @Query("per_page") perPage: Int = Constants.PER_PAGE_VALUE,
        @Query("format") format: String = Constants.FORMAT_VALUE,
        @Query("extras") extras: String = Constants.EXTRAS_VALUE,
        @Query("nojsoncallback") nojsoncallback: String = Constants.NO_JSON_CALLBACK_VALUE
    ): FlickrPhotosResponse
}