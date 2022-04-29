package com.example.bd

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding:ActivityLoginBinding

    //Action Bar
    private lateinit var actionBar: ActionBar

    //Progress Dialog
    private lateinit var progressDialog:ProgressDialog

    //Firebase auth
    private lateinit var firebaseAuth:FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor aguarde...")
        progressDialog.setMessage("A entrar ....")
        progressDialog.setCanceledOnTouchOutside(false)

        //init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //ao clicar na tv de registar
        binding.noAccountTv.setOnClickListener{
            startActivity(Intent(this, SignUp::class.java))
        }

        //ao clicar no btn login
        binding.loginBtn.setOnClickListener {
            //validar a informação
            validateData()
        }
    }

    private fun validateData() {
        //obter informação
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordET.text.toString().trim()

        //validação
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email invalido
            binding.emailEt.error = "Email no formato inválido"
        }else if (TextUtils.isEmpty(password)){
            //sem pass
            binding.passwordET.error = "Por favor insira uma palavra pass"
        }else{
            //dados validados, proceder ao login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //Mostrar Progresso
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //Login sucesso
                progressDialog.dismiss()

                //obtem a informação do utilizador
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                Toast.makeText(this, "Login com o email: $email", Toast.LENGTH_SHORT).show()

                //abre o perfil
                startActivity(Intent(this, Profile::class.java))
                finish()

            }
            .addOnFailureListener{ e->
                //Login Falha
                progressDialog.dismiss()
                Toast.makeText(this, "O Login falhou ao fazer ${e.message}", Toast.LENGTH_SHORT).show()

            }
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
}