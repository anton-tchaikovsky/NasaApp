package com.example.nasaapp.model.repository

import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import retrofit2.Callback
import java.util.*

fun interface RepositoryAstronomyPictureOfTheDay {
    fun getAstronomyPictureOfTheDay(date:Calendar)
}