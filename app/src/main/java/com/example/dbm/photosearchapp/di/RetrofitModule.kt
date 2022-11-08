package com.example.dbm.photosearchapp.di

import com.example.dbm.photosearchapp.data.datasource.RemoteAPI
import com.example.dbm.photosearchapp.util.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String, mosh: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(mosh))
            .build()
    }

    @Singleton
    @Provides
    fun provideMoviesService(mosh: Moshi): RemoteAPI {
        return provideRetrofit(Constants.BASE_URL, mosh).create(RemoteAPI::class.java)
    }
}