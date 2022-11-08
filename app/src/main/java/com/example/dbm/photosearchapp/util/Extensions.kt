package com.example.dbm.photosearchapp.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.data.model.PhotoNetwork
import java.text.SimpleDateFormat
import java.util.*

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

fun PhotoNetwork.getImageUrl(): String {
    return if(this.urlH != null && this.urlH != ""){
        this.urlH ?: ""
    } else if (this.urlO != null && this.urlO != "") {
        this.urlO ?: ""
    } else if (this.urlC != null && this.urlC != "") {
        this.urlC ?: ""
    } else {
        ""
    }
}

fun String.formatDate(): String {

    val onlyDateString = this.substring(0, 10)
    val oldFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val newFormatter = SimpleDateFormat("EEE dd yyyy", Locale.US)

    val date = oldFormatter.parse(onlyDateString)

    return newFormatter.format(date ?: Date())
}