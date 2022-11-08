package com.example.dbm.photosearchapp.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.databinding.FragmentPhotoDetailsBinding
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.presentation.viewmodel.PhotosViewModel
import com.example.dbm.photosearchapp.util.loadImage
import kotlinx.coroutines.launch

class PhotoDetailsFragment: Fragment(R.layout.fragment_photo_details) {

    private val viewModel: PhotosViewModel by activityViewModels()
    private var _binding: FragmentPhotoDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPhotoDetailsBinding.bind(view)
        _binding = binding

        setupObservers()
    }

    private fun setupObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if(state.validShownIndex){
                    val item = state.listPhotos[state.shownIndex]
                    updateUI(item)
                }
            }
        }
    }

    private fun updateUI(item: PhotoView){
        _binding?.photoItemImageView?.loadImage(item.imgUrl)
    }
}