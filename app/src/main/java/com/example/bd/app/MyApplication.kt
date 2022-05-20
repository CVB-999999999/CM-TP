package com.example.bd.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.example.bd.*
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        fun bottomMenu(bottomNavigationView: BottomNavigationView, context: Context){

            //ao clicar em um item
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.dashboard -> {
                        val intent = Intent(context, Defenicoes::class.java)
                        context.startActivity(intent)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.home -> {
                        val intent = Intent(context, StudentList::class.java)
                        context.startActivity(intent)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.favourite -> {
                        val intent = Intent(context, StdFavoritosList::class.java)
                        context.startActivity(intent)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.qrCode -> {
                        val intent = Intent(context, StudentList::class.java)
                        context.startActivity(intent)
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.search -> {
                        val intent = Intent(context, SearchActivity::class.java)
                        context.startActivity(intent)
                        return@setOnNavigationItemSelectedListener true
                    }
                    else -> return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
}