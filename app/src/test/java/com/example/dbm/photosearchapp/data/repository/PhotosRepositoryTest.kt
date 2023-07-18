package com.example.dbm.photosearchapp.data.repository

import androidx.test.filters.SmallTest
import com.example.dbm.photosearchapp.data.datasource.RemoteAPI
import com.example.dbm.photosearchapp.data.model.FlickrPhotosResponse
import com.example.dbm.photosearchapp.data.model.PhotoNetwork
import com.example.dbm.photosearchapp.data.model.PhotosGroupNetwork
import com.example.dbm.photosearchapp.domain.repository.IPhotosRepository
import com.example.dbm.photosearchapp.util.ResultWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
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

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@RunWith(MockitoJUnitRunner::class)
class PhotosRepositoryTest {

    @Mock
    lateinit var remoteAPI: RemoteAPI

    lateinit var SUT: IPhotosRepository

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        SUT = PhotosRepository(remoteAPI, UnconfinedTestDispatcher())
    }

    @Test
    fun getPhotosFromFeed_successfulResponse_returnListFromAPI(){
        return runTest {
            val job = launch {
                sendNetworkResponse(false)
            }

            job.join()

            val response = SUT.getPhotosFromFeed() as ResultWrapper.Success

            verify(remoteAPI, times(1)).getPhotosFromFeed()
            assertThat(response.value.size).isEqualTo(5)
            assertThat(response.value[0].id).isEqualTo(0)
            assertThat(response.value[0].title).isEqualTo("Title 0")
            assertThat(response.value[0].author).isEqualTo("Owner Name 0")
            assertThat(response.value[0].date).isEqualTo("Tue 18 2022")
            assertThat(response.value[1].id).isEqualTo(1)
            assertThat(response.value[1].title).isEqualTo("Title 1")
            assertThat(response.value[1].author).isEqualTo("Owner Name 1")
            assertThat(response.value[1].date).isEqualTo("Mon 17 2022")
            assertThat(response.value[2].id).isEqualTo(2)
            assertThat(response.value[2].title).isEqualTo("Title 2")
            assertThat(response.value[2].author).isEqualTo("Owner Name 2")
            assertThat(response.value[2].date).isEqualTo("Sun 16 2022")
        }
    }

    @Test
    fun getPhotosFromFeed_failedResponse_returnEmptyList(){
        return runTest {
            val job = launch {
                sendNetworkResponse(true)
            }

            job.join()

            val response = SUT.getPhotosFromFeed() as ResultWrapper.Failure
            verify(remoteAPI, times(1)).getPhotosFromFeed()
            assertThat(response.exception?.message).isEqualTo("An error occurred in networkResponse")
        }
    }

    private suspend fun sendNetworkResponse(withError: Boolean) {
        if(withError) {
            Mockito.`when`(remoteAPI.getPhotosFromFeed()).thenAnswer {
                throw IOException("An error occurred in networkResponse")
            }
        } else {
            val listPhotosNetwork = ArrayList<PhotoNetwork>()

            val flickrPhotosResponse = FlickrPhotosResponse(stat = "ok")
            val photosGroupNetwork = PhotosGroupNetwork()

            photosGroupNetwork.photos = listPhotosNetwork

            flickrPhotosResponse.photosGroupNetwork = photosGroupNetwork

            listPhotosNetwork.add(PhotoNetwork(id = "0", title = "Title 0", datetaken = "2022-10-18 09:56:01", ownername = "Owner Name 0", urlH = "URL H0"))
            listPhotosNetwork.add(PhotoNetwork(id = "1", title = "Title 1", datetaken = "2022-10-17 07:56:01", ownername = "Owner Name 1", urlH = "URL H1"))
            listPhotosNetwork.add(PhotoNetwork(id = "2", title = "Title 2", datetaken = "2022-10-16 05:56:01", ownername = "Owner Name 2", urlH = "URL H2"))
            listPhotosNetwork.add(PhotoNetwork(id = "3", title = "Title 3", datetaken = "2022-10-15 03:56:01", ownername = "Owner Name 3", urlH = "URL H3"))
            listPhotosNetwork.add(PhotoNetwork(id = "4", title = "Title 4", datetaken = "2022-10-14 09:56:01", ownername = "Owner Name 4", urlH = "URL H4"))

            Mockito.`when`(remoteAPI.getPhotosFromFeed()).thenReturn(
                flickrPhotosResponse
            )
        }
    }
}