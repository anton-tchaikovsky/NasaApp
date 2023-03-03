package com.example.nasaapp.model.remote_data_source

import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.utils.*
import retrofit2.Call
import retrofit2.http.*

interface APIMarsRoverPhotos {
    @GET(END_POINT_MARS_ROVER_PHOTOS)
    fun getMarsRoverPhotos(
        @Query(EARTH_DATE) date:String,
        @Query(API_NASA_KEY) apiNasaKey:String
    ): Call<MarsRoverPhotos>
}