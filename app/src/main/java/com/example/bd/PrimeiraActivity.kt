package com.example.bd

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivityLoginBinding
import com.example.bd.databinding.ActivityPrimeiraBinding
import com.google.firebase.auth.FirebaseAuth

class PrimeiraActivity : AppCompatActivity() {

    //Action Bar
    private lateinit var actionBar: ActionBar

    //viewBinding
    private lateinit var binding: ActivityPrimeiraBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_primeira)

        //configure actionbar
       // actionBar = supportActionBar!!
       // actionBar.title = "Rental Student"

        binding = ActivityPrimeiraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


    }

    private fun checkUser() {
        //Se já estiver logado vai direto para o perfil
        //obter o current user (esqueci-me com é a tradução)
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //utilizador já logado
            startActivity(Intent(this, Profile::class.java))
            finish()
        }
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