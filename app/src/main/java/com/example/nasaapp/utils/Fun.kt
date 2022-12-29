package com.example.nasaapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun convertDateStringFromCalendar(date:Calendar):String{
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        return formatter.format(date.time)
}