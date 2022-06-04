package com.example.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.bd.databinding.ActivityForgetBinding
import com.example.bd.databinding.ActivityPrimeiraBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetBinding

    //Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        binding = ActivityForgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.enviarBtn.setOnClickListener {
            onBackPressed()
            //trim vai remover espaços em branco
            email = binding.emailEt.text.toString().trim {it <= ' '}
            if (email.isEmpty()) {
                Toast.makeText(this@ForgetActivity, "Por favor introduza o email.", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this@ForgetActivity, "Email enviado com sucesso para redefinir a sua password!", Toast.LENGTH_SHORT).show()

                            finish()
                        }else{
                            Toast.makeText(this@ForgetActivity, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}