package com.example.dbm.photosearchapp.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosFromFeedUseCase
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.io.IOException
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosBySearchTermUseCase
import dagger.hilt.android.testing.BindValue

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@RunWith(MockitoJUnitRunner::class)
class PhotosViewModelTest {

    @Mock
    lateinit var getPhotosFromFeedUseCaseMock: IGetPhotosFromFeedUseCase

    @Mock
    lateinit var getPhotosBySearchTermUseCaseMock: IGetPhotosBySearchTermUseCase

    lateinit var SUT: PhotosViewModel

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        SUT = PhotosViewModel(getPhotosFromFeedUseCaseMock, getPhotosBySearchTermUseCaseMock, UnconfinedTestDispatcher())
    }

    @Test
    fun getPhotosFromFeed_successfulResponse_sendListToUI(){
        return runTest {
            val job = launch {
                successfulResponse()
            }

            job.join()

            assertThat(SUT.uiState.value.isLoading).isEqualTo(true)

            SUT.getPhotosFromFeed()

            verify(getPhotosFromFeedUseCaseMock, times(1)).invoke()
            assertThat(SUT.uiState.value.isLoading).isEqualTo(false)
            assertThat(SUT.uiState.value.errorPresent).isEqualTo(false)
            assertThat(SUT.uiState.value.messageWrapper.messageResource).isEqualTo(0)
            assertThat(SUT.uiState.value.listPhotos[0].title).isEqualTo("Title 0")
            assertThat(SUT.uiState.value.listPhotos[1].date).isEqualTo("Date 1")
            assertThat(SUT.uiState.value.listPhotos[2].author).isEqualTo("Author 2")
            assertThat(SUT.uiState.value.listPhotos[3].imgUrl).isEqualTo("Image Url 3")
        }
    }

    @Test
    fun getPhotosFromFeed_failedResponse_sendErrorMessageToUI(){
        return runTest {
            val job = launch {
                failureResponse()
            }

            job.join()

            assertThat(SUT.uiState.value.isLoading).isEqualTo(true)

            SUT.getPhotosFromFeed()

            verify(getPhotosFromFeedUseCaseMock, times(1)).invoke()
            assertThat(SUT.uiState.value.isLoading).isEqualTo(false)
            assertThat(SUT.uiState.value.errorPresent).isEqualTo(true)
            assertThat(SUT.uiState.value.messageWrapper.messageResource).isEqualTo(R.string.error_message)
        }
    }

    private suspend fun successfulResponse() {

        val listPhotoView = ArrayList<PhotoView>()

        listPhotoView.add(PhotoView(0,"Title 0", "Date 0", "Author 0", "Image Url 0"))
        listPhotoView.add(PhotoView(1,"Title 1", "Date 1", "Author 1", "Image Url 1"))
        listPhotoView.add(PhotoView(2,"Title 2", "Date 2", "Author 2", "Image Url 2"))
        listPhotoView.add(PhotoView(3,"Title 3", "Date 3", "Author 3", "Image Url 3"))


        Mockito.`when`(getPhotosFromFeedUseCaseMock.invoke()).thenReturn(
           listPhotoView
        )
    }

    private suspend fun failureResponse() {
        Mockito.`when`(getPhotosFromFeedUseCaseMock.invoke()).thenAnswer {
            throw IOException("An error occurred in the ViewModel test")
        }
    }
}