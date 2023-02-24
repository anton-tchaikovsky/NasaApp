package com.example.nasaapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.api.load
import com.example.nasaapp.databinding.ItemMarsPhotoFhacBinding
import com.example.nasaapp.databinding.ItemMarsPhotoMastBinding
import com.example.nasaapp.databinding.ItemMarsPhotoRhacBinding
import com.example.nasaapp.model.dto.mars.Photo
import com.example.nasaapp.utils.FHAC
import com.example.nasaapp.utils.MAST
import com.example.nasaapp.utils.RHAZ

class AdapterForMarsRoverPhotosRecyclerViewFragment( private var listPhoto: List<Photo>, private val addRemoveCallback: MarsRoverPhotosRecyclerViewFragment.AddRemove ):
    RecyclerView.Adapter<AdapterForMarsRoverPhotosRecyclerViewFragment.MarsViewHolder>() {

    fun setListPhotoAfterAdd(newListPhoto: List<Photo>, position: Int){
        listPhoto = newListPhoto
        notifyItemInserted(position)
    }

    fun setListPhotoAfterRemove(newListPhoto: List<Photo>, position: Int){
        listPhoto = newListPhoto
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
       return when (listPhoto[position].camera.fullName){
           MAST -> 1
           FHAC -> 2
           RHAZ ->3
           else -> 3
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsViewHolder {
        return when (viewType){
            1 -> MarsPhotoMastViewHolder(ItemMarsPhotoMastBinding.inflate(LayoutInflater.from(parent.context)))
            2 -> MarsPhotoFhacViewHolder(ItemMarsPhotoFhacBinding.inflate(LayoutInflater.from(parent.context)))
            3 -> MarsPhotoRhacViewHolder(ItemMarsPhotoRhacBinding.inflate(LayoutInflater.from(parent.context)))
            else -> MarsPhotoRhacViewHolder(ItemMarsPhotoRhacBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: MarsViewHolder, position: Int) {
        holder.bind(listPhoto[position])
    }

    override fun getItemCount(): Int = listPhoto.size

    abstract class MarsViewHolder(open val binding: ViewBinding):RecyclerView.ViewHolder(binding.root){
      abstract fun bind(photo: Photo)
    }

    inner class MarsPhotoMastViewHolder (override val binding: ItemMarsPhotoMastBinding):MarsViewHolder(binding) {
        override fun bind(photo: Photo) {
            binding.run {
                camera.text = photo.camera.fullName
                marsPhoto.load(photo.imgSrc)
                add.setOnClickListener{addRemoveCallback.addItem(layoutPosition)}
                remove.setOnClickListener{
                   addRemoveCallback.removeItem(layoutPosition)}
            }
        }
    }

    class MarsPhotoFhacViewHolder (override val binding: ItemMarsPhotoFhacBinding):MarsViewHolder(binding) {
        override fun bind(photo: Photo) {
            binding.run {
                camera.text = photo.camera.fullName
                marsPhoto.load(photo.imgSrc)
            }
        }
    }

    class MarsPhotoRhacViewHolder (override val binding: ItemMarsPhotoRhacBinding):MarsViewHolder(binding) {
        override fun bind(photo: Photo) {
            binding.run {
                camera.text = photo.camera.fullName
                marsPhoto.load(photo.imgSrc)
            }
        }
    }
}