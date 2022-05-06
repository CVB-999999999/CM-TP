package com.example.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class PrimeiraActivity : AppCompatActivity() {

    //Action Bar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_primeira)

        //configure actionbar
       // actionBar = supportActionBar!!
       // actionBar.title = "Rental Student"
    }
}