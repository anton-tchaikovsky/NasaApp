package com.example.nasaapp.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera
import com.example.nasaapp.model.repository.RepositoryEarthPhotosImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthPhotosViewModel(private val liveData: MutableLiveData<AppStateEarthPhotos> = MutableLiveData<AppStateEarthPhotos>()) :
    ViewModel() {

    fun getLiveData() = liveData

    fun getEarthPhotos() {
        liveData.value = AppStateEarthPhotos.Loading
        repository.getEarthPhotos()
    }

    private val callback = object : Callback<EarthPolychromaticImagingCamera> {
        override fun onResponse(
            call: Call<EarthPolychromaticImagingCamera>,
            response: Response<EarthPolychromaticImagingCamera>
        ) {
            if (response.isSuccessful && response.body() != null)
                liveData.postValue(AppStateEarthPhotos.Success(response.body() as EarthPolychromaticImagingCamera))
            else
                liveData.postValue(AppStateEarthPhotos.Error(IllegalStateException()))
        }

        override fun onFailure(call: Call<EarthPolychromaticImagingCamera>, t: Throwable) {
            liveData.postValue(AppStateEarthPhotos.Error(t))
        }

    }
    private val repository = RepositoryEarthPhotosImpl(callback)

}