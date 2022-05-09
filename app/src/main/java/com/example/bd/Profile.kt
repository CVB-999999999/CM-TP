package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivityProfileBinding
import com.example.bd.models.studentList
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class Profile : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivityProfileBinding

    //Action Bar
   // private lateinit var actionBar: ActionBar

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure action bar
       // actionBar = supportActionBar!!
        //actionBar.title="Perfil"

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //btn logout
        binding.logoutBtn.setOnClickListener {

            firebaseAuth.signOut()
            checkUser()
        }

        //btn editar Perfil
        binding.btnEditar.setOnClickListener {
            //startActivity(Intent(this, EditarPerfil::class.java))
            //startActivity(Intent(this, CriarQuarto::class.java))
            startActivity(Intent(this, StudentList::class.java))
        }


        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.dashboard)

        //ao clicar em um item
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> {
                        startActivity(Intent(this, Profile::class.java))
                        overridePendingTransition(0, 0)
                        return@setOnNavigationItemSelectedListener true
                    }
                R.id.home -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourite -> {
                    startActivity(Intent(this, StudentList::class.java))
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

        //Ativa o modo imersivo (Para mais Info Ide pesquisar na documentação do androi studio)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

    }

    //Funçao a testar (ao sair e voltar a entrar, caso não ative o modo imersivo auto, descomentar isto)
    //override fun onWindowFocusChanged(hasFocus: Boolean) {
    //    super.onWindowFocusChanged(hasFocus)
    //    if (hasFocus){
    //        window.decorView.apply {
    //            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    //        }
    //    }
    //}

    private fun checkUser() {
        //check if user is logged or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user not null
            val email = firebaseUser.email

            //envia para a text view
            binding.emailTv.text = email
        }else{
            //user logout
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
