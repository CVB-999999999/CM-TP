package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivityProfileBinding
import com.example.bd.models.studentList
import com.google.firebase.auth.FirebaseAuth


class Profile : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivityProfileBinding

    //Action Bar
   // private lateinit var actionBar: ActionBar

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

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
    }

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