package com.example.nasaapp.utils

import java.text.SimpleDateFormat
import java.util.*

// метод преобразовывает объект Calendar в строковую дату по шаблону "yyyy-MM-dd"
fun convertDateStringFromCalendar(date:Calendar):String{
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        return formatter.format(date.time)
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