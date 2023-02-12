package com.example.nasaapp.model.dto.mars

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarsRoverPhotos(
    val photos: List<Photo>
):Parcelable