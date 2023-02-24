package com.example.nasaapp.model.remote_data_source

import com.example.nasaapp.BuildConfig
import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.utils.URL_API_NASA
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSourceMarsRoverPhotos {

    //строим ретрофит и создаем объект класса APIMarsRoverPhotos
    private val apiMarsRoverPhotos by lazy { Retrofit.Builder()
        .baseUrl(URL_API_NASA)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build().create(APIMarsRoverPhotos::class.java) }

    //создаем запрос типа call для любого дня по дате и отправляем его в очередь на получение ответа
    fun callAPIMarsRoverPhotos(date:String, callback: Callback<MarsRoverPhotos>){
        apiMarsRoverPhotos.getMarsRoverPhotos(date, BuildConfig.api_nasa_key).enqueue(callback)
    }
}