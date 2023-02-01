package com.example.nasaapp.model.repository

import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera
import com.example.nasaapp.model.remote_data_source.RemoteDataSourceEarthPhotos
import retrofit2.Callback

class RepositoryEarthPhotosImpl(private val callback: Callback<EarthPolychromaticImagingCamera>):RepositoryEarthPhotos {
    override fun getEarthPhotos() {
        RemoteDataSourceEarthPhotos().callAPIEarthPhotos(callback)
    }
}