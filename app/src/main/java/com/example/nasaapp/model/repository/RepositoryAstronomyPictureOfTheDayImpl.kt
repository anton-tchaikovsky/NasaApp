package com.example.nasaapp.model.repository

import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.model.remote_data_source.RemoteDataSourceAstronomyPicturesOfTheDay
import com.example.nasaapp.utils.convertDateStringFromCalendar
import retrofit2.Callback
import java.util.*

class RepositoryAstronomyPictureOfTheDayImpl(private val callback: Callback<AstronomyPictureOfTheDay>):RepositoryAstronomyPictureOfTheDay {
    override fun getAstronomyPictureOfTheDay(date: Calendar) =
        RemoteDataSourceAstronomyPicturesOfTheDay().callAPIAstronomyPicturesOfTheDay(convertDateStringFromCalendar(date), callback)
}