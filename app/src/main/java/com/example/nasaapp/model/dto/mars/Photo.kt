package com.example.nasaapp.model.dto.mars

import com.google.gson.annotations.SerializedName

data class Photo(
    @field:SerializedName("earth_date") val earthDate: String,
    @field:SerializedName("img_src") val imgSrc: String,
)