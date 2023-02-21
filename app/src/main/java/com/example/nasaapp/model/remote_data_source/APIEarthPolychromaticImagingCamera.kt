package com.example.nasaapp.model.remote_data_source

import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera
import com.example.nasaapp.utils.API_NASA_KEY
import com.example.nasaapp.utils.END_POINT_EARTH_POLYCHROMATIC_IMAGING_CAMERA
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIEarthPolychromaticImagingCamera {
    @GET(END_POINT_EARTH_POLYCHROMATIC_IMAGING_CAMERA)
    fun getEarthPhotos(
        @Query(API_NASA_KEY) apiNasaKey:String
    ): Call<EarthPolychromaticImagingCamera>
}