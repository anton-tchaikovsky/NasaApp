package com.example.nasaapp.model.dto.mars

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Camera(
    @field:SerializedName("full_name")val fullName: String,
): Parcelable