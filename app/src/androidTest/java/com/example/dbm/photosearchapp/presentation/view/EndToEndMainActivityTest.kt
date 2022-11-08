package com.example.dbm.photosearchapp.presentation.view

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.di.DispatchersModule
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosFromFeedUseCase
import com.example.dbm.photosearchapp.presentation.viewmodel.PhotosViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Inject
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosBySearchTermUseCase
import com.example.dbm.photosearchapp.presentation.view.adapter.PhotosAdapter
import org.hamcrest.CoreMatchers.not

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@LargeTest
@RunWith(MockitoJUnitRunner::class)
class EndToEndMainActivityTest {

    private lateinit var context: Context
    private lateinit var countingIdlingResource: CountingIdlingResource
    private lateinit var scenario: ActivityScenario<MainActivity>

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var getPhotosFromFeedUseCase: IGetPhotosFromFeedUseCase

    @Inject
    lateinit var getPhotosBySearchTermUseCase: IGetPhotosBySearchTermUseCase

    @Inject
    @DispatchersModule.MainDispatcher
    lateinit var dispatcher: CoroutineDispatcher

    @BindValue
    lateinit var viewModel: PhotosViewModel

    @Before
    fun setUp(){
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        viewModel = PhotosViewModel(getPhotosFromFeedUseCase, getPhotosBySearchTermUseCase, dispatcher)
        countingIdlingResource = CountingIdlingResource("PhotosGridCall")
        viewModel.setIdlingResourceAPICall(countingIdlingResource)
        IdlingRegistry.getInstance().register(countingIdlingResource)
    }

    @Test
    fun getPhotos_successfulResponse_gridIsShown(){
        return runTest {
            val job = launch {
                scenario = ActivityScenario.launch(MainActivity::class.java)
            }

            job.join()

            onView(withId(R.id.progress_bar))
                .check(matches(not(isDisplayed())))
            onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))
            onView(withId(R.id.progress_bar_message))
                .check(matches(not(isDisplayed())))
            onView(withId(R.id.empty_message))
                .check(matches(not(isDisplayed())))
        }
    }

    @Test
    fun getPhotos_successfulResponse_itemInGridClickedDetailsFragmentAppears(){
        return runTest {
            val job = launch {
                scenario = ActivityScenario.launch(MainActivity::class.java)
            }

            job.join()

            onView(withId(R.id.progress_bar))
                .check(matches(not(isDisplayed())))
            onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))
            onView(withId(R.id.progress_bar_message))
                .check(matches(not(isDisplayed())))
            onView(withId(R.id.empty_message))
                .check(matches(not(isDisplayed())))

            onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<PhotosAdapter.PhotosViewHolder>(
                        3, click()))

            onView(withId(R.id.photo_item_image_view)).check(matches(isDisplayed()))

            onView(isRoot()).perform(pressBack())
            onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))

            scenario.recreate()

            onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))
        }
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
        if(this::scenario.isInitialized){
            scenario.close()
        }
    }
}