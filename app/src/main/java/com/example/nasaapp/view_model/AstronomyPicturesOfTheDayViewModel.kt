package com.example.nasaapp.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.dto.AstronomyPictureOfTheDay
import com.example.nasaapp.model.repository.RepositoryAstronomyPictureOfTheDayImplRemote
import com.example.nasaapp.utils.Day
import com.example.nasaapp.utils.convertCalendarFromDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AstronomyPicturesOfTheDayViewModel(private val liveData: MutableLiveData<AppStateAstronomyPicturesOfTheDay> = MutableLiveData<AppStateAstronomyPicturesOfTheDay>()) :
    ViewModel() {

    fun getLiveData() = liveData

    fun getAstronomyPicturesOfTheDay(day: Day) {
        liveData.value = AppStateAstronomyPicturesOfTheDay.Loading
        repository.getAstronomyPictureOfTheDay(convertCalendarFromDay(day))
    }

    private val callback = object : Callback<AstronomyPictureOfTheDay> {
        override fun onResponse(
            call: Call<AstronomyPictureOfTheDay>,
            response: Response<AstronomyPictureOfTheDay>
        ) {
            if (response.isSuccessful && response.body() != null)
                liveData.postValue(AppStateAstronomyPicturesOfTheDay.Success(response.body() as AstronomyPictureOfTheDay))
            else
                liveData.postValue(AppStateAstronomyPicturesOfTheDay.Error(IllegalStateException()))
        }

        override fun onFailure(call: Call<AstronomyPictureOfTheDay>, t: Throwable) {
            liveData.postValue(AppStateAstronomyPicturesOfTheDay.Error(t))
        }

    }
    private val repository = RepositoryAstronomyPictureOfTheDayImplRemote(callback)

}