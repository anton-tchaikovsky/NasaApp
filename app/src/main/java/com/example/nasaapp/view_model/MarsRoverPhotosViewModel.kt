package com.example.nasaapp.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.dto.mars.MarsRoverPhotos
import com.example.nasaapp.model.dto.mars.Photo
import com.example.nasaapp.model.repository.RepositoryMarsRoverPhotosImpl
import com.example.nasaapp.utils.Day
import com.example.nasaapp.utils.convertCalendarFromDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverPhotosViewModel(private val liveData: MutableLiveData<AppStateMarsRoverPhotos> = MutableLiveData<AppStateMarsRoverPhotos>(),
                               private val liveDataCountMarsRoverPhotos:MutableLiveData<Int> = MutableLiveData<Int>()) :
    ViewModel() {

   companion object{
       var marsRoverPhotos:MarsRoverPhotos? = null
       var listPhoto: MutableList<Photo> = mutableListOf()
   }

    fun getLiveData() = liveData

    fun getLiveDataCountMarsRoverPhotos() = liveDataCountMarsRoverPhotos

    fun getMarsRoverPhotos(day: Day) {
        liveData.value = AppStateMarsRoverPhotos.Loading
        repository.getMarsRoverPhotos(convertCalendarFromDay(day))
    }

    fun getCountMarsRoverPhotos(day: Day){
        repositoryCountMarsRoverPhotos.getMarsRoverPhotos(convertCalendarFromDay(day))
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

    private val callbackCountMarsRoverPhotos = object : Callback<MarsRoverPhotos> {
        override fun onResponse(
            call: Call<MarsRoverPhotos>,
            response: Response<MarsRoverPhotos>
        ) {
            if (response.isSuccessful && response.body() != null)
                (response.body() as MarsRoverPhotos).run {
                    marsRoverPhotos = this
                    liveDataCountMarsRoverPhotos.postValue(photos.size)
                }
            else
                liveDataCountMarsRoverPhotos.postValue(0)
        }

        override fun onFailure(call: Call<MarsRoverPhotos>, t: Throwable) {
            liveDataCountMarsRoverPhotos.postValue(0)
        }

    }
    private val repository = RepositoryMarsRoverPhotosImpl(callback)
    private val repositoryCountMarsRoverPhotos = RepositoryMarsRoverPhotosImpl(callbackCountMarsRoverPhotos)

    fun getListPhoto():List<Photo>{
        marsRoverPhotos?.photos?.let{
            it.forEach {photo->
                if (listPhoto.size<10)
                    listPhoto.add(photo)
                else return listPhoto
            }
        }
        return listPhoto
    }

    fun addPhoto(position:Int): List<Photo>{
        marsRoverPhotos?.photos?.let {
            if (it.size> listPhoto.size){
                listPhoto.add(position, it[listPhoto.size])
            }
        }
        return listPhoto
    }

    fun removePhoto(position:Int): List<Photo>{
        listPhoto.removeAt(position)
        return listPhoto
    }

    fun movePhoto(fromPosition: Int, toPosition: Int):List<Photo>{
        listPhoto.removeAt(fromPosition).apply {
            listPhoto.add(toPosition, this)
        }
        return listPhoto
    }

}