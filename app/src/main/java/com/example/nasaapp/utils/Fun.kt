package com.example.nasaapp.utils

import java.text.SimpleDateFormat
import java.util.*

// метод преобразовывает объект Calendar в строковую дату по шаблону "yyyy-MM-dd",
// при этом время приводится к timeZone "America/New_York" (для NasaAPI)
fun convertDateStringFromCalendar(calendar:Calendar):String{
    val timeZone = TimeZone.getTimeZone(TIME_ZONE_NEW_YORK)
    calendar.timeZone = timeZone
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply {
        this.timeZone = timeZone
    }
        return formatter.format(calendar.time)
}

// метод преобразовывает объект Day (enum) в объект Calendar с датой: сегодня, вчера или позавчера
fun convertCalendarFromDay(day:Day):Calendar{
    val calendar = Calendar.getInstance()
    return when(day){
        Day.TODAY -> calendar
        Day.YESTERDAY -> calendar.apply { add(Calendar.DAY_OF_YEAR, -1) }
        Day.DAY_BEFORE_YESTERDAY -> calendar.apply { add(Calendar.DAY_OF_YEAR, -2) }
    }
}