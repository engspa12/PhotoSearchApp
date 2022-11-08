package com.example.dbm.photosearchapp.util

import com.example.dbm.photosearchapp.BuildConfig

object Constants {
    const val SORT_VALUE = "relevance"
    const val PER_PAGE_VALUE = 300
    const val BASE_URL = "https://www.flickr.com/services/"
    const val API_KEY_VALUE = BuildConfig.API_KEY
    const val FORMAT_VALUE = "json"
    const val EXTRAS_VALUE = "owner_name,date_taken,url_h,url_c,url_o"
    const val NO_JSON_CALLBACK_VALUE = "1"
}