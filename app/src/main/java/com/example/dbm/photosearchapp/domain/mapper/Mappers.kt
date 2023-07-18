package com.example.dbm.photosearchapp.domain.mapper

import com.example.dbm.photosearchapp.data.model.PhotoNetwork
import com.example.dbm.photosearchapp.domain.model.PhotoDomain
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun PhotoNetwork.getImageUrl(): String {
    return if(!this.urlH.isNullOrEmpty()){
        this.urlH.orEmpty()
    } else if (!this.urlO.isNullOrEmpty()) {
        this.urlO.orEmpty()
    } else if (!this.urlC.isNullOrEmpty()) {
        this.urlC.orEmpty()
    } else {
        ""
    }
}

fun String.formatDate(): String {

    val oldFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val newFormatter = SimpleDateFormat("EEE dd yyyy", Locale.US)

    val date: Date = try {
        val onlyDateString = this.substring(0, 10)
        oldFormatter.parse(onlyDateString) ?: Date()
    } catch (e: ParseException){
        Date()
    } catch (e: IndexOutOfBoundsException){
        Date()
    }

    return newFormatter.format(date)
}
