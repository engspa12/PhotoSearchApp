package com.example.dbm.photosearchapp.util

import android.content.Context
import androidx.annotation.StringRes

data class MessageWrapper(
    @StringRes val messageResource: Int = 0,
    val argForResource: String? = null
) {
    fun asString(context: Context): String{
        return if(argForResource != null){
            context.getString(messageResource, argForResource) ?: ""
        } else {
            context.getString(messageResource) ?: ""
        }
    }
}