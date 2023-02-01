package com.example.nasaapp.model.remote_data_source

import com.example.nasaapp.BuildConfig
import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera
import com.example.nasaapp.utils.URL_API_NASA
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceEarthPhotos {

    //строим ретрофит и создаем объект класса APIEarthPhotos
    private val apiEarthPhotos by lazy { Retrofit.Builder()
        .baseUrl(URL_API_NASA)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build().create(APIEarthPolychromaticImagingCamera::class.java) }

    //создаем запрос типа call для любого дня по дате и отправляем его в очередь на получение ответа
    fun callAPIEarthPhotos(callback: Callback<EarthPolychromaticImagingCamera>){
        apiEarthPhotos.getEarthPhotos(BuildConfig.api_nasa_key).enqueue(callback)
    }
}