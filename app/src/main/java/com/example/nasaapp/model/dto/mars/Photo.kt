package com.example.nasaapp.model.dto.mars

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    @field:SerializedName("earth_date") val earthDate: String,
    @field:SerializedName("img_src") val imgSrc: String,
    val camera: Camera
):Parcelable