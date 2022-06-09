package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bd.databinding.ActivityAvaliarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

class AvaliarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAvaliarBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAvaliarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codAnuncio = intent.getStringExtra("codAnuncio")

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.avaliarBtn.setOnClickListener {
            enviarAvaliacao(codAnuncio!!)
        }
    }

    private var codAvaliacao = UUID.randomUUID().toString()
    private var coment = ""

    private fun enviarAvaliacao(codAnuncio: String) {

        val timeStamp = System.currentTimeMillis()
        val uId = firebaseAuth.uid

        var geral = binding.geralRT.rating
        var espaco = binding.espacoRT.rating
        var proprietario = binding.proprietarioRT.rating
        coment = binding.comentariosEt.text.toString().trim()

        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["codAvaliacao"]= codAvaliacao
        hashMap["codAnuncio"]=codAnuncio
        hashMap["idUtilizador"]=uId
        hashMap["geral"]=geral
        hashMap["proprietario"]=proprietario
        hashMap["espaco"]=espaco
        hashMap["comentario"]=coment
        hashMap["visiblidade"]="1" // visiblidade do anuncio 0-oculto || 1-visivel
        hashMap["dataPublicacao"]=timeStamp


        //Toast.makeText(this, hashMap.toString(), Toast.LENGTH_SHORT).show()
        //guardar td
        val ref = FirebaseDatabase.getInstance().getReference("Avaliacoes")
        ref.child(codAvaliacao!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //caso de sucesso
                Toast.makeText(this, R.string.avaliacaoRegistada, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VerAnuncio::class.java)
                intent.putExtra("codAnuncio", codAnuncio)
                startActivity(intent)
            }
            .addOnFailureListener {
                //caso de fail
                Toast.makeText(this, R.string.avaliacaoFail, Toast.LENGTH_SHORT).show()
            }
    }
}