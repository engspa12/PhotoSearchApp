package com.example.dbm.photosearchapp.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.databinding.FragmentPhotosGridBinding
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.presentation.view.adapter.PhotosAdapter
import com.example.dbm.photosearchapp.presentation.viewmodel.PhotosViewModel
import com.example.dbm.photosearchapp.util.MessageWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosGridFragment: Fragment(R.layout.fragment_photos_grid), PhotosAdapter.PhotoOnClickListener {

    private val viewModel: PhotosViewModel by activityViewModels()
    private var _binding: FragmentPhotosGridBinding? = null
    private val adapter = PhotosAdapter(listener = this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPhotosGridBinding.bind(view)
        _binding = binding

        setupRecyclerView()
        setupObservers()
        getPhotos()
    }

    private fun getPhotos(){
        viewModel.getPhotosFromFeed()
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(context, 2)

        _binding?.recyclerView?.layoutManager = gridLayoutManager
        _binding?.recyclerView?.adapter = adapter
        _binding?.recyclerView?.setHasFixedSize(true)
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state ->
                    //Update list of items with new content
                    if(state.listHasChanged) {
                        updateRecyclerView(state.listPhotos)
                    }

                    handleProgressBar(state.isLoading)
                    handleEmptyListMessage(state.listPhotos.isEmpty(), state.isLoading)
                    handleErrorMessage(state.errorPresent, state.isLoading, state.errorMessage)
                }
            }
        }
    }

    private fun handleProgressBar(isLoading: Boolean){
        _binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        _binding?.progressBarMessage?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleErrorMessage(errorPresent: Boolean, isLoading: Boolean, errorMessage: MessageWrapper){
        if(errorPresent){
            _binding?.recyclerView?.visibility = View.GONE
            _binding?.emptyMessage?.visibility = if (isLoading) View.GONE else View.VISIBLE
            _binding?.emptyMessage?.text = errorMessage.asString(requireContext())
        } else {
            _binding?.recyclerView?.visibility = View.VISIBLE
            _binding?.emptyMessage?.visibility = View.GONE
        }
    }

    private fun handleEmptyListMessage(listIsEmpty: Boolean, isLoading: Boolean){
        if(listIsEmpty){
            _binding?.recyclerView?.visibility = View.GONE
            _binding?.emptyMessage?.visibility = if (isLoading) View.GONE else View.VISIBLE
            _binding?.emptyMessage?.text = getString(R.string.list_is_empty)
        } else {
            _binding?.recyclerView?.visibility = View.VISIBLE
            _binding?.emptyMessage?.visibility = View.GONE
        }
    }

    private fun updateRecyclerView(list: List<PhotoView>){
        adapter.updateDataSet(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        viewModel.setSelectedIndex(position)
        val fragment = parentFragmentManager.findFragmentByTag("DetailsFrag")

        if(fragment == null){
            val newFragment = PhotoDetailsFragment()
            parentFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container, newFragment, "DetailsFrag")
                .addToBackStack(null)
                .commit()
        }

        viewModel.listWasShown()
    }
}