package com.example.nasaapp.model.remote_data_source

import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.*
import retrofit2.Call
import retrofit2.http.*

interface APIAstronomyPicturesOfTheDay {
    @GET(END_POINT)
    fun getAstronomyPicturesOfTheDay(
        @Header(API_NASA_KEY) apiNasaKey:String,
        @Query(DATE) date:String
    ): Call<AstronomyPictureOfTheDay>
}