package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivityPrimeiraBinding

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

    fun goLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun goRegistar(view: View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
    }
}