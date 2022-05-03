package com.example.bd

import android.app.ProgressDialog
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivitySignUp2Binding

    //Action Bar
    private lateinit var actionBar: ActionBar

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        actionBar = supportActionBar!!
        actionBar.title="Registar"

        //back btn
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure ProgressDialog
        //configure progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor aguarde...")
        progressDialog.setMessage("A registar ....")
        progressDialog.setCanceledOnTouchOutside(false)

        //init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //quando clicat começa o registo
        binding.SignBtn.setOnClickListener {
                //valida a informação
                validateData()
        }
    }

    private fun validateData() {
        //obter dados
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordET.text.toString().trim()

        //validar
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email invalido
            binding.emailEt.error = "Email no formato inválido"
        }else if (TextUtils.isEmpty(password)){
            //sem pass
            binding.passwordET.error = "Por favor insira uma palavra pass"
        }else if (password.length<6){
            //sem comprimento suficiente
            binding.passwordET.error = "Por favor insira uma palavra pass com pelo menos 6 caracteres"
        }else{
            //dados validados
            firebaseRegistar()
        }

    }

    private fun firebaseRegistar() {
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //sucesso registou
                progressDialog.dismiss()
                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                Toast.makeText(this, "Conta registada com o email: $email", Toast.LENGTH_SHORT).show()

                //abre o perfil
                startActivity(Intent(this, Profile::class.java))
                finish()
            }
            .addOnFailureListener {e->
                //falhou
                progressDialog.dismiss()
                Toast.makeText(this, "O registou falhou no ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //volta para a atividade anterior
        return super.onSupportNavigateUp()
    }
}