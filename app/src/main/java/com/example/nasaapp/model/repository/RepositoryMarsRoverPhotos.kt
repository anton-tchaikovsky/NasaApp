package com.example.nasaapp.model.repository

import java.util.*

fun interface RepositoryMarsRoverPhotos {
    fun getMarsRoverPhotos(date:Calendar)
}