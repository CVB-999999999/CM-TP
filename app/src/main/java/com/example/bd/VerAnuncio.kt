package com.example.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.bd.databinding.ActivityProfileBinding
import com.example.bd.databinding.ActivityVerAnuncioBinding
import com.example.bd.models.studentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerAnuncio : AppCompatActivity() {

    private lateinit var binding: ActivityVerAnuncioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerAnuncioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codAnuncio = intent.getStringExtra("codAnuncio")

        binding.titulo.text = codAnuncio.toString()

        loadAnuncio(codAnuncio!!)

    }

    private fun loadAnuncio(codAnuncio: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.child(codAnuncio!!)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = "${snapshot.child("email").value}"
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}