package com.example.dbm.photosearchapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoNetwork(
    @Json(name = "id")
    var id: String? = null,
    @Json(name = "owner")
    var owner: String? = null,
    @Json(name = "secret")
    var secret: String? = null,
    @Json(name = "server")
    var server: String? = null,
    @Json(name = "farm")
    var farm: Int? = null,
    @Json(name = "title")
    var title: String? = null,
    @Json(name = "ispublic")
    var ispublic: Int? = null,
    @Json(name = "isfriend")
    var isfriend: Int? = null,
    @Json(name = "isfamily")
    var isfamily: Int? = null,
    @Json(name = "datetaken")
    var datetaken: String? = null,
    @Json(name = "datetakengranularity")
    var datetakengranularity: Int? = null,
    @Json(name = "datetakenunknown")
    var datetakenunknown: String? = null,
    @Json(name = "ownername")
    var ownername: String? = null,
    @Json(name = "url_h")
    var urlH: String? = null,
    @Json(name = "height_h")
    var heightH: Int? = null,
    @Json(name = "width_h")
    var widthH: Int? = null,
    @Json(name = "url_c")
    var urlC: String? = null,
    @Json(name = "height_c")
    var heightC: Int? = null,
    @Json(name = "width_c")
    var widthC: Int? = null,
    @Json(name = "url_o")
    var urlO: String? = null,
    @Json(name = "height_o")
    var heightO: Int? = null,
    @Json(name = "width_o")
    var widthO: Int? = null
)