package com.example.nasaapp.utils

import android.view.Surface
import com.example.nasaapp.R

enum class Theme(val id:Int, val title:String, val idColorPrimary:Int, val idColorPrimaryContainer:Int, val idColorSurface: Int ) {
    THEME_RED(R.style.Theme_NasaAppRed, THEME_RED_TITLE, R.color.primary_theme_red, R.color.primary_container_theme_red, R.color.surface_theme_red),
    THEME_BLUE(R.style.Theme_NasaAppBlue, THEME_BLUE_TITLE, R.color.primary_theme_blue, R.color.primary_container_theme_blue, R.color.surface_theme_blue),
    THEME_ORANGE(R.style.Theme_NasaAppOrange, THEME_ORANGE_TITLE, R.color.primary_theme_orange, R.color.primary_container_theme_orange, R.color.surface_theme_orange)
}