package com.example.bd

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.bd.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivitySignUp2Binding

    //Action Bar
   // private lateinit var actionBar: ActionBar

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password =""
    private var nome = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
       // actionBar = supportActionBar!!
       // actionBar.title="Registar"

        //back btn
        //actionBar.setDisplayHomeAsUpEnabled(true)
        //actionBar.setDisplayShowHomeEnabled(true)

        //configure ProgressDialog
        //configure progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(R.string.wait)
        progressDialog.setMessage(R.string.registe.toString())
        progressDialog.setCanceledOnTouchOutside(false)

        //init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        //quando clicat começa o registo
        binding.SignBtn.setOnClickListener {
                //valida a informação
                validateData()
        }
        //Ativa o modo imersivo
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

    }

    private fun validateData() {
        //obter dados
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordET.text.toString().trim()
        nome = binding.nomeEt.text.toString().trim()

        //validar
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email invalido
            binding.emailEt.error = "Email " + R.string.fomatInvalid
        }else if (TextUtils.isEmpty(password)){
            //sem pass
            binding.passwordET.error = R.string.insertPass.toString()
        }else if (password.length<6){
            //sem comprimento suficiente
            binding.passwordET.error = R.string.insertPassCaracter.toString()
        }else if (TextUtils.isEmpty(nome)){
            //sem nome
            binding.emailEt.error = R.string.insertName.toString()
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
                ////sucesso registou
                //progressDialog.dismiss()
                ////get current user
                //val firebaseUser = firebaseAuth.currentUser
                //val email = firebaseUser!!.email
//
                //Toast.makeText(this, "Conta registada com o email: $email", Toast.LENGTH_SHORT).show()
//
                ////abre o perfil
                //startActivity(Intent(this, Profile::class.java))
                //finish()
                updateUserInfo()
            }
            .addOnFailureListener {
                //falhou
                progressDialog.dismiss()
                Toast.makeText(this, R.string.registerFail, Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {
        //guarda os restantes dados no real time database
        progressDialog.setMessage(R.string.save.toString())

        val timeStamp = System.currentTimeMillis()
        val uId = firebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["idUtilizador"]=uId
        hashMap["nome"]=nome
        hashMap["email"]=email
        hashMap["telemovel"]= " " //para já fica assim, depois muda-se os dados
        hashMap["password"]=password
        hashMap["localizacao"]="Viana do Castelo"
        hashMap["fotoPerfil"]=" "
        hashMap["timeStamp"]=timeStamp
        hashMap["tipoUtilizador"]=0 //0-aluno | 1-anunciante

        //guardar td
        val ref = FirebaseDatabase.getInstance().getReference("Utilizadores")
        ref.child(uId!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //caso de sucesso
                progressDialog.dismiss()
                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                Toast.makeText(this, R.string.registeredAccount, Toast.LENGTH_SHORT).show()

                ////abre o perfil
                startActivity(Intent(this, StudentList::class.java))
                finish()
            }
            .addOnFailureListener {
                //caso de fail
                progressDialog.dismiss()
                Toast.makeText(this, R.string.registerFail, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //volta para a atividade anterior
        return super.onSupportNavigateUp()
    }
}