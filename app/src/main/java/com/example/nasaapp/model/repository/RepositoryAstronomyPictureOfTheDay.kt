package com.example.nasaapp.model.repository

import java.util.*

fun interface RepositoryAstronomyPictureOfTheDay {
    fun getAstronomyPictureOfTheDay(date:Calendar)
}