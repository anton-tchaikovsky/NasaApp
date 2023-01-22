package com.example.nasaapp.view_model

import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay

sealed class AppStateAstronomyPicturesOfTheDay{
    data class Success(val astronomyPicturesOfTheDay:AstronomyPictureOfTheDay):AppStateAstronomyPicturesOfTheDay()
    data class Error (val error:Throwable):AppStateAstronomyPicturesOfTheDay()
    object Loading:AppStateAstronomyPicturesOfTheDay()
}
