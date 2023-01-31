package com.example.nasaapp.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.model.repository.RepositoryMarsRoverPhotosImpl
import com.example.nasaapp.utils.Day
import com.example.nasaapp.utils.convertCalendarFromDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverPhotosViewModel(private val liveData: MutableLiveData<AppStateMarsRoverPhotos> = MutableLiveData<AppStateMarsRoverPhotos>()) :
    ViewModel() {

    fun getLiveData() = liveData

    fun getMarsRoverPhotos(day: Day) {
        liveData.value = AppStateMarsRoverPhotos.Loading
        repository.getMarsRoverPhotos(convertCalendarFromDay(day))
    }

    private val callback = object : Callback<MarsRoverPhotos> {
        override fun onResponse(
            call: Call<MarsRoverPhotos>,
            response: Response<MarsRoverPhotos>
        ) {
            if (response.isSuccessful && response.body() != null)
                liveData.postValue(AppStateMarsRoverPhotos.Success(response.body() as MarsRoverPhotos))
            else
                liveData.postValue(AppStateMarsRoverPhotos.Error(IllegalStateException()))
        }

        override fun onFailure(call: Call<MarsRoverPhotos>, t: Throwable) {
            liveData.postValue(AppStateMarsRoverPhotos.Error(t))
        }

    }
    private val repository = RepositoryMarsRoverPhotosImpl(callback)

}