package com.example.nasaapp.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nasaapp.utils.Day

class ViewPagerAdapterForAstronomyPicturesOfTheDay(fragment: Fragment, private val listDay: Array<Day>):
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = listDay.size

    override fun createFragment(position: Int): Fragment = AstronomyPicturesOfTheDayFragment.newInstance(listDay[position])
}