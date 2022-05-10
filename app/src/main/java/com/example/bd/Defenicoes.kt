package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.bd.databinding.ActivityDefenicoesBinding
import com.example.bd.databinding.ActivityEditarPerfilBinding
import com.example.bd.models.studentList
import com.google.android.material.bottomnavigation.BottomNavigationView

class Defenicoes : AppCompatActivity() {
    private lateinit var binding: ActivityDefenicoesBinding

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDefenicoesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.dashboard)

        //ao clicar em um item
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> {
                    startActivity(Intent(this, Defenicoes::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourite -> {
                    startActivity(Intent(this, StdFavoritosList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.qrCode -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        //Ativa o modo imersivo
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        binding.criarAnuncio.setOnClickListener {
            startActivity(Intent(this, CriarQuarto::class.java))
        }

        binding.editContaLL.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }
    }
}