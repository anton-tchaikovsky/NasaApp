package com.example.nasaapp.model.remote_data_source

import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.utils.*
import retrofit2.Call
import retrofit2.http.*

interface APIAstronomyPicturesOfTheDay {
    @GET(END_POINT)
    fun getAstronomyPicturesOfTheDay(
        @Query(DATE) date:String,
        @Query(API_NASA_KEY) apiNasaKey:String
    ): Call<AstronomyPictureOfTheDay>
}