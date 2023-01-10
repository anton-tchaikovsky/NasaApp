package com.example.nasaapp.model.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AstronomyPictureOfTheDay(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdurl: String,
    @field:SerializedName("media_type")
    val mediaType: String,
    @field:SerializedName("service_version")
    val serviceVersion: String,
    val title: String,
    val url: String
):Parcelable