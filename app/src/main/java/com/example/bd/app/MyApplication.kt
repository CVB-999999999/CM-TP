package com.example.bd.app

import android.app.Application
import android.text.format.DateFormat
import java.util.*

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object{

        //formatar TimeStamp
        fun formatTimeStamp(timestamp: Long) : String{
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = timestamp

            //formato dd/mm/aaaa
            return DateFormat.format("dd/MM/yyyy", cal).toString()
        }


    }
}