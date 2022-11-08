package com.example.dbm.photosearchapp.presentation.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.di.DispatchersModule
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosBySearchTermUseCase
import com.example.dbm.photosearchapp.domain.usecase.IGetPhotosFromFeedUseCase
import com.example.dbm.photosearchapp.presentation.state.PhotosUIState
import com.example.dbm.photosearchapp.util.MessageWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosFromFeedUseCase: IGetPhotosFromFeedUseCase,
    private val getPhotosBySearchTermUseCase: IGetPhotosBySearchTermUseCase,
    @DispatchersModule.MainDispatcher private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow(PhotosUIState(isLoading = true))
    val uiState: StateFlow<PhotosUIState> = _uiState

    private var fetchJob: Job? = null
    private var countingIdlingResource: CountingIdlingResource? = null

    fun getPhotosBySearchTerm(searchTerm: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(mainDispatcher) {
            try {
                val result = getPhotosBySearchTermUseCase(searchTerm = searchTerm)
                _uiState.update {
                    if(result.isEmpty()){
                        it.copy(listPhotos = result, listHasChanged = true, messageWrapper = MessageWrapper(R.string.no_search_results_for, searchTerm))
                    } else {
                        it.copy(listPhotos = result, listHasChanged = true, messageWrapper = MessageWrapper(R.string.search_results_for, searchTerm))
                    }
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(errorPresent = true, messageWrapper = MessageWrapper(R.string.error_message))
                }
            }
        }
    }

    fun getPhotosFromFeed() {
        incrementIdlingResourceAPICall()
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(mainDispatcher) {
            try {
                val result = getPhotosFromFeedUseCase()
                _uiState.update {
                    it.copy(listPhotos = result, listHasChanged = true, isLoading = false)
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(errorPresent = true, messageWrapper = MessageWrapper(R.string.error_message), isLoading = false)
                }
            }
            //Delay added to wait for images to be completely loaded
            delay(1500L)
            decrementIdlingResourceAPICall()
        }
    }

    fun listWasShown() {
        _uiState.update {
            it.copy(listHasChanged = false)
        }
    }

    fun setSelectedIndex(newIndex: Int){
        _uiState.update {
            it.copy(shownIndex = newIndex, validShownIndex = true, listHasChanged = false)
        }
    }

    private fun incrementIdlingResourceAPICall(){
        countingIdlingResource?.increment()
    }

    private fun decrementIdlingResourceAPICall(){
        countingIdlingResource?.decrement()
    }

    @VisibleForTesting
    fun setIdlingResourceAPICall(countingIdlingResource: CountingIdlingResource){
        this.countingIdlingResource = countingIdlingResource
    }
}