@file:Suppress("DEPRECATION")

package com.example.nasaapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.nasaapp.utils.Day

class ViewPagerAdapterForAstronomyPicturesOfTheDay(fragmentManager: FragmentManager, private val listDay: Array<Day>):FragmentStatePagerAdapter(fragmentManager,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = listDay.size

    override fun getItem(position: Int): Fragment = AstronomyPicturesOfTheDayFragment.newInstance(listDay[position])

    override fun getPageTitle(position: Int): CharSequence = listDay[position].day
}