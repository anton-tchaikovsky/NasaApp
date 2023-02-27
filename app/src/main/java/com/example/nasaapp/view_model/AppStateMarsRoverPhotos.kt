package com.example.nasaapp.view_model

import com.example.nasaapp.model.dto.mars.MarsRoverPhotos

sealed class AppStateMarsRoverPhotos{
    data class Success(val marsRoverPhotos: MarsRoverPhotos):AppStateMarsRoverPhotos()
    data class Error (val error:Throwable):AppStateMarsRoverPhotos()
    object Loading:AppStateMarsRoverPhotos()
}
