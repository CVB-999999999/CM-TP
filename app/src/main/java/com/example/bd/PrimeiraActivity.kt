package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivityPrimeiraBinding

class PrimeiraActivity : AppCompatActivity() {

    //Action Bar
    private lateinit var actionBar: ActionBar

    private lateinit var binding: ActivityPrimeiraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_primeira)

        //configure actionbar
       // actionBar = supportActionBar!!
       // actionBar.title = "Rental Student"

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegistar.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

    }
}