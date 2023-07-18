package com.example.dbm.photosearchapp.presentation.view

import android.content.Context
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dbm.photosearchapp.di.UseCasesModule
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosFromFeedUseCase
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.presentation.viewmodel.PhotosViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosBySearchTermUseCase
import com.example.dbm.photosearchapp.domain.util.PhotosDomainError
import com.example.dbm.photosearchapp.launchFragmentInHiltContainer
import com.example.dbm.photosearchapp.util.ResultWrapper
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
@UninstallModules(UseCasesModule::class)
@HiltAndroidTest
@LargeTest
@RunWith(MockitoJUnitRunner::class)
class PhotosGridFragmentIntegrationTest {

    private lateinit var context: Context
    private lateinit var scenario: FragmentScenario<PhotosGridFragment>

    @BindValue @Mock
    lateinit var getPhotosFromFeedUseCaseMock: IGetPhotosFromFeedUseCase

    @BindValue @Mock
    lateinit var getPhotosBySearchTermUseCaseMock: IGetPhotosBySearchTermUseCase

    @BindValue
    lateinit var viewModel: PhotosViewModel

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = PhotosViewModel(getPhotosFromFeedUseCaseMock, getPhotosBySearchTermUseCaseMock, UnconfinedTestDispatcher())
    }

    @Test
    fun getPhotos_obtainGridOfPhotos_successfulResponse(){
        return runTest {

            successfulResponse()

            launchFragmentInHiltContainer<PhotosGridFragment> {

            }

            onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
            onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
            onView(withId(R.id.progress_bar_message)).check(matches(not(isDisplayed())))
            onView(withId(R.id.empty_message)).check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun getPhotos_obtainErrorMessage_failedResponse(){
        return runTest {
            failureResponse()

            launchFragmentInHiltContainer<PhotosGridFragment> {

            }

            onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
            onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())))
            onView(withId(R.id.progress_bar_message)).check(matches(not(isDisplayed())))
            onView(withId(R.id.empty_message)).check(matches(isDisplayed()))
            onView(withText(context.getString(R.string.generic_error_message))).check(matches(isDisplayed()))
        }
    }

    @After
    fun tearDown(){
        if(this::scenario.isInitialized){
            scenario.close()
        }
    }

    private suspend fun successfulResponse(){

        val list = ArrayList<PhotoView>()

        list.add(PhotoView(id = 0, title = "Title 0", date = "Date 0", author = "Author 0", imgUrl = "https://live.staticflickr.com/65535/52480205761_e74f960290_h.jpg"))
        list.add(PhotoView(id = 1, title = "Title 1", date = "Date 1", author = "Author 1", imgUrl = "https://live.staticflickr.com/65535/52480379166_064b2412c2_h.jpg"))
        list.add(PhotoView(id = 2, title = "Title 2", date = "Date 2", author = "Author 2", imgUrl = "https://live.staticflickr.com/65535/52479899264_0d99a7645b_h.jpg"))
        list.add(PhotoView(id = 3, title = "Title 3", date = "Date 3", author = "Author 3", imgUrl = "https://live.staticflickr.com/65535/52480965393_bb5ecc0466_h.jpg"))

        Mockito.`when`(getPhotosFromFeedUseCaseMock.invoke()).thenReturn(
            ResultWrapper.Success(list)
        )
    }

    private suspend fun failureResponse() {
        Mockito.`when`(getPhotosFromFeedUseCaseMock.invoke()).thenReturn(
            ResultWrapper.Failure(error = PhotosDomainError.GENERIC_ERROR)
        )
    }
}