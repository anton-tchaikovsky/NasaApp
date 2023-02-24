package com.example.nasaapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.nasaapp.databinding.ItemMarsPhotoBinding
import com.example.nasaapp.model.dto.mars.Photo

class AdapterForMarsRoverPhotosRecyclerViewFragment( var listPhoto: List<Photo> ):
    RecyclerView.Adapter<AdapterForMarsRoverPhotosRecyclerViewFragment.MarsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        return MarsViewHolder(ItemMarsPhotoBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        holder.bind(listPhoto[position])
    }

    override fun getItemCount(): Int = listPhoto.size

    class MarsViewHolder(val binding: ItemMarsPhotoBinding):RecyclerView.ViewHolder(binding.root){
       fun bind(photo: Photo){
            binding.run {
                camera.text = photo.camera.fullName
                marsPhoto.load(photo.imgSrc)
            }
        }
    }
}