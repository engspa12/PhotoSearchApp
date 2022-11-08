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
import com.example.dbm.photosearchapp.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            when(val result = getPhotosBySearchTermUseCase(searchTerm = searchTerm)){
                is ResultWrapper.Success -> {
                    _uiState.update {
                        if(result.value.isEmpty()){
                            it.copy(listPhotos = result.value, listHasChanged = true, titleText = MessageWrapper(R.string.no_search_results_for, searchTerm), isLoading = false)
                        } else {
                            it.copy(listPhotos = result.value, listHasChanged = true, titleText = MessageWrapper(R.string.search_results_for, searchTerm), isLoading = false)
                        }
                    }
                }
                is ResultWrapper.Failure -> {
                    _uiState.update {
                        it.copy(errorPresent = true, errorMessage = result.errorMessage, isLoading = false)
                    }
                }
            }
        }
    }

    fun getPhotosFromFeed() {
        incrementIdlingResourceAPICall()
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(mainDispatcher) {
            when(val result = getPhotosFromFeedUseCase()){
                is ResultWrapper.Success -> {
                    _uiState.update {
                        it.copy(listPhotos = result.value, listHasChanged = true, isLoading = false)
                    }
                }
                is ResultWrapper.Failure -> {
                    _uiState.update {
                        it.copy(errorPresent = true, errorMessage = result.errorMessage, isLoading = false)
                    }
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