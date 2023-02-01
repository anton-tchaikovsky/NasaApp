package com.example.nasaapp.view_model

import com.example.nasaapp.model.dto.earth.EarthPolychromaticImagingCamera

sealed class AppStateEarthPhotos{
    data class Success(val earthPolychromaticImagingCamera: EarthPolychromaticImagingCamera):AppStateEarthPhotos()
    data class Error (val error:Throwable):AppStateEarthPhotos()
    object Loading:AppStateEarthPhotos()
}
