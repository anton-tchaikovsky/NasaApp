package com.example.nasaapp.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Toast
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

// метод проверяет наличие интернет-соединения
@Suppress("DEPRECATION")
fun isConnectNetwork(context: Context?):Boolean{
    val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val connectInfo = connectivityManager.activeNetworkInfo
    return (connectInfo != null) && connectInfo.isConnectedOrConnecting
}

// метод показывает и скрывает переданные View
fun hideShowViews(hideViews:List<View>, showViews: List<View>){
    hideViews.forEach {
        it.visibility = View.GONE
    }
    showViews.forEach {
        it.visibility = View.VISIBLE
    }
}

fun showToast (context: Context?, message:String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).apply {
        setGravity(Gravity.BOTTOM, 0, 150)
        show()
    }
}

fun dpToPixels(dpValue: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpValue,
        Resources.getSystem().displayMetrics
    ).toInt()
}