package com.example.nasaapp.model.dto.mars

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarsRoverPhotos(
    var photos: List<Photo>
):Parcelable