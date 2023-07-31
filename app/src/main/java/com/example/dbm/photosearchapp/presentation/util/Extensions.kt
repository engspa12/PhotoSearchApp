package com.example.dbm.photosearchapp.presentation.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dbm.photosearchapp.R

fun ImageView.loadImage(url: String){

    val options = RequestOptions()
        .placeholder(R.drawable.placeholder_image)
        .error(R.drawable.no_image_available)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .override(580, 320)
        .centerCrop()

    Glide
        .with(this)
        .load(url)
        .apply(options)
        .into(this)
}

fun PhotosViewError.mapToStringResource(): Int{
    return when(this){
        PhotosViewError.GENERIC_ERROR -> R.string.generic_error_message
        PhotosViewError.UNKNOWN_ERROR -> R.string.error_unknown
        else -> R.string.generic_error_message
    }
}