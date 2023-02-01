package com.example.nasaapp.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nasaapp.model.dto.mars.MarsRoverPhotos

class ViewPagerAdapterForMarsRoverPhotos(fragment: Fragment, private val marsRoverPhotos: MarsRoverPhotos):
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = marsRoverPhotos.photos.size

    override fun createFragment(position: Int): Fragment = MarsRoverPhotosFragment.newInstance(marsRoverPhotos.photos[position].imgSrc)
}