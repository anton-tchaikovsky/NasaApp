package com.example.nasaapp.model.repository

import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.model.remote_data_source.RemoteDataSourceMarsRoverPhotos
import com.example.nasaapp.utils.convertDateStringFromCalendar
import retrofit2.Callback
import java.util.*

class RepositoryMarsRoverPhotosImpl(private val callback: Callback<MarsRoverPhotos>):RepositoryMarsRoverPhotos {
    override fun getMarsRoverPhotos(date: Calendar) {
        RemoteDataSourceMarsRoverPhotos().callAPIMarsRoverPhotos("2023-02-28", callback)
    }
}