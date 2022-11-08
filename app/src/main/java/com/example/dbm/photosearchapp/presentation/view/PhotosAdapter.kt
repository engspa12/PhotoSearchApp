package com.example.dbm.photosearchapp.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dbm.photosearchapp.R
import com.example.dbm.photosearchapp.presentation.model.PhotoView
import com.example.dbm.photosearchapp.util.loadImage

class PhotosAdapter(inputList: List<PhotoView> = listOf(), private val listener: PhotoOnClickListener): RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {

    private var innerList: List<PhotoView>

    interface PhotoOnClickListener{
        fun onItemClick(position: Int)
    }

    init {
        innerList = inputList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_layout, parent, false)

        return PhotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val urlImage = innerList[position].imgUrl
        holder.gridImageView.loadImage(
            urlImage
        )
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return innerList.size
    }

    fun updateDataSet(newList: List<PhotoView>){
        innerList = newList
        notifyDataSetChanged()
    }

    class PhotosViewHolder(view: View): ViewHolder(view) {
        val gridImageView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            gridImageView = view.findViewById(R.id.grid_image_view)
        }
    }
}