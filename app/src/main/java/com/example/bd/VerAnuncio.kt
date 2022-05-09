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
import com.squareup.picasso.Picasso

class VerAnuncio : AppCompatActivity() {

    private lateinit var binding: ActivityVerAnuncioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerAnuncioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codAnuncio = intent.getStringExtra("codAnuncio")

        loadAnuncio(codAnuncio!!)

    }

    private fun loadAnuncio(codAnuncio: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.child(codAnuncio!!)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //carrega os dados
                val dataAtualizacao = "${snapshot.child("dataAtualizacao").value}"
                val dataCriacao = "${snapshot.child("dataCriacao").value}"
                val descricao = "${snapshot.child("descricao").value}"
                val email = "${snapshot.child("email").value}"
                val idUtilizador = "${snapshot.child("idUtilizador").value}"
                val morada = "${snapshot.child("morada").value}"
                val preco = "${snapshot.child("preco").value}"
                val rAnimais = "${snapshot.child("rAnimais").value}"
                val rAcessivel = "${snapshot.child("rAcessivel").value}"
                val rFumadores = "${snapshot.child("rFumadores").value}"
                val rPreco = "${snapshot.child("rPreco").value}"
                val reservado = "${snapshot.child("reservado").value}"
                val telemovel = "${snapshot.child("telemovel").value}"
                val titulo = "${snapshot.child("titulo").value}"


                //coloca os dados
                binding.titulo.text = titulo
                binding.localizacao.text = morada
                binding.preco.text = preco + " €"
                binding.descricaoEt.text = descricao
                binding.contactoEmailET.text = "Email: " + email
                binding.contactoTelET.text = "Telemóvel: " + telemovel

                //coloca as regras
                if (rAnimais.equals(true)) {
                    binding.pet.setImageResource(R.drawable.ic_baseline_people_24)
                }

                if (rFumadores.equals(true)) {
                    binding.smoke.setImageResource(R.drawable.ic_baseline_smoking_rooms_24)
                } else {
                    binding.smoke.setImageResource(R.drawable.ic_baseline_smoke_free_24)
                }
                if (rAcessivel.equals(true)) {
                    binding.accessible.setImageResource(R.drawable.ic_baseline_accessible_24)
                } else {
                    binding.accessible.setImageResource(R.drawable.ic_baseline_not_accessible_24)
                }
                if (rPreco.equals(true)) {
                    binding.negociavel.setImageResource(R.drawable.ic_baseline_attach_money_24)
                } else {
                    binding.negociavel.setImageResource(R.drawable.ic_baseline_money_off_24)
                }

                // Male only
                if (reservado.equals("0")) {
                    binding.male.setImageResource(R.drawable.ic_baseline_male_24)

                // Female only
                } else if (reservado.equals("1")) {
                    binding.male.setImageResource(R.drawable.ic_baseline_male_24)

                // Both
                } else {
                    binding.male.setImageResource(R.drawable.ic_baseline_male_24)
                    binding.female.setImageResource(R.drawable.ic_baseline_female_24)
                }

                carregaImagemCapa(codAnuncio)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun carregaImagemCapa(codAnuncio: String) {

        //atraves do codigo do anuncio vou buscar uma imagem a BD
        val ref = FirebaseDatabase.getInstance().getReference("ImagensAnuncios")
        ref.child(codAnuncio!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imagem = "https://firebasestorage.googleapis.com/v0/b/rentalstudent-47413.appspot.com/o/imagensAnuncios%2Fvazio.jpg?alt=media&token=18b47cd5-7b63-43d2-964f-35f69008848a"
                    if (snapshot.exists()) {
                        val imagem = "${snapshot.child("imagemURL").value}"
                    }

                    //carrega a imagem no anuncio
                    Picasso.get().load(imagem).into(binding.imagemA)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}