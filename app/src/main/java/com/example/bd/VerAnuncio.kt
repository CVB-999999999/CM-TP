package com.example.bd

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bd.adapters.avaliacoesAdapter
import com.example.bd.adapters.studentListAdapter
import com.example.bd.databinding.ActivityVerAnuncioBinding
import com.example.bd.models.avaliacoesModel
import com.example.bd.models.studentList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class VerAnuncio : AppCompatActivity() {

    private lateinit var avaliacoesListAdapter: avaliacoesAdapter

    //arraylist para o holder
    private lateinit var avaliacoesArrayList: ArrayList<avaliacoesModel>


    private lateinit var binding: ActivityVerAnuncioBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var viewFlipperCapa: ViewFlipper

    private lateinit var imageView: ImageView
    private lateinit var imageVC: ImageView

    //aramzena o valor se esta ou não nos favoritos, começa como falso
    private var isInMyFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerAnuncioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codAnuncio = intent.getStringExtra("codAnuncio")

        viewFlipper = binding.vFlipper
        viewFlipperCapa = binding.imagemA

        carregaImgCapa(codAnuncio!!)

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
            checkFav(codAnuncio!!)
        }

        loadAnuncio(codAnuncio!!)

        //Favoritos
        binding.favorite.setOnClickListener {
            if (firebaseAuth.currentUser == null) {
                Toast.makeText(this, "Necessita de ter login efetuado!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, PrimeiraActivity::class.java))
            } else {
                if (isInMyFavorite) {
                    //remove
                    removeFromFav(codAnuncio)
                } else {
                    //adiciona
                    addFavorite(codAnuncio)
                }
            }
        }

        //quando para voltar para tras
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.avaliacoesBtn.setOnClickListener {
            if (firebaseAuth.currentUser == null){
                Toast.makeText(this, "Necessita de ter login efetuado!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, PrimeiraActivity::class.java))
            }else{
                val intent = Intent(this, AvaliarActivity::class.java)
                intent.putExtra("codAnuncio", codAnuncio)
                startActivity(intent)
            }
        }

        binding.editarBtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, binding.editarBtn)
            popupMenu.menuInflater.inflate(R.menu.menu_editar, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.editar -> {
                        Toast.makeText(this, "Editar", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, EditarQuarto::class.java)
                        intent.putExtra("codAnuncio", codAnuncio)
                        startActivity(intent)
                    }
                    R.id.eliminar -> {
                        Toast.makeText(this, "Eliminar", Toast.LENGTH_SHORT).show()
                        removeAnuncio(codAnuncio)
                        startActivity(Intent(this, StudentList::class.java))
                        finish()
                    }
                }
                true
            })
            popupMenu.show()
        }

        binding.voltarIMG.setOnClickListener {
            viewFlipper.showPrevious()
        }

        binding.proximaIMG.setOnClickListener {
            viewFlipper.showNext()
        }

        binding.ligarBt.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_DIAL)
                    .setData(Uri.parse("tel:" + binding.contactoTelET.text))
            )
        }


        avaliacoesListAdapter = avaliacoesAdapter(ArrayList())
        val recyclerView: RecyclerView = binding.avaliacoesLine
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = avaliacoesListAdapter
        avaliacoesListAdapter.notifyDataSetChanged()

        //carrega os anuncios
        avaliacoesArrayList = arrayListOf<avaliacoesModel>()
        avaliacoesListAdapter.rmAll()
        loadList(codAnuncio!!)

    }

    private fun loadList(codAnuncio: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Avaliacoes")
        // Live Update
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Vai carregar todos os dados
                    for (avaliacaoSnap in snapshot.children) {
                        val visiblidade = "${avaliacaoSnap.child("visiblidade").value}"
                        val codA = "${avaliacaoSnap.child("codAnuncio").value}"

                        if (visiblidade.equals("1") && codA.equals(codAnuncio!!)) { //verifica se o anuncio está no estado 1
                            val anuncio = avaliacaoSnap.getValue(avaliacoesModel::class.java)
                            avaliacoesArrayList.add(anuncio!!)

                        }
                    }
                    //carrega para view
                    avaliacoesArrayList.forEach {
                        avaliacoesListAdapter.addTodo(it)
                    }
                }
            }
            //  ERRO
            override fun onCancelled(error: DatabaseError) {

            }
        })
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

                    if(idUtilizador.equals(firebaseAuth.uid)){
                        binding.editarBtn.setVisibility(View.VISIBLE)
                        binding.ligarBt.setVisibility(View.INVISIBLE)
                        binding.avaliacoesBtn.setVisibility(View.INVISIBLE)
                    }else{
                        binding.editarBtn.setVisibility(View.INVISIBLE)
                        binding.ligarBt.setVisibility(View.VISIBLE)
                        binding.avaliacoesBtn.setVisibility(View.VISIBLE)
                    }
                    //carregaImagemCapa(codAnuncio)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun carregaImgCapa(codAnuncio: String) {
        val ref = FirebaseDatabase.getInstance().getReference("ImagensAnuncios")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var conta = 0
                    for (anuncioSnap in snapshot.children) {
                        val codA = "${anuncioSnap.child("codAnuncio").value}"
                        if (codA.equals(codAnuncio)) {
                            val imagem = "${anuncioSnap.child("imagemURL").value}"
                            //Picasso.get().load(imagem).into(imageView)
                            flipperImage(imagem)
                            flipperImageCapa(imagem)
                            conta = conta + 1
                        }
                    }
                    if (conta == 0) {
                        val imagem =
                            "https://firebasestorage.googleapis.com/v0/b/rentalstudent-47413.appspot.com/o/imagensAnuncios%2F0c31a1fea169f62723961552742988b3a97e8e12.jpg?alt=media&token=ee27cce2-f4be-4801-aa1a-b7ce98516852"
                        flipperImage(imagem)
                        flipperImageCapa(imagem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkFav(codAnuncio: String) {

        val rel = FirebaseDatabase.getInstance().getReference("Utilizadores")
        rel.child(firebaseAuth.uid!!).child("Favoritos").child(codAnuncio)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    isInMyFavorite = snapshot.exists()

                    if (isInMyFavorite) {
                        //já existia
                        binding.favorite.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_baseline_favorite_24,
                            0,
                            0
                        )
                    } else {
                        binding.favorite.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            R.drawable.ic_outline_favorite_border_24,
                            0,
                            0
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun addFavorite(codAnuncio: String) {

        val timestamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["codAnuncio"] = codAnuncio
        hashMap["timestamp"] = timestamp
        //guardar na BD
        val rel = FirebaseDatabase.getInstance().getReference("Utilizadores")
        rel.child(firebaseAuth.uid!!).child("Favoritos").child(codAnuncio)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Anuncio adicionado aos favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeFromFav(codAnuncio: String) {

        val rel = FirebaseDatabase.getInstance().getReference("Utilizadores")
        rel.child(firebaseAuth.uid!!).child("Favoritos").child(codAnuncio)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Anuncio removido dos favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
            }

    }

    private fun flipperImage(imagem: String) {

        imageView = ImageView(this)
        Picasso.get().load(imagem).into(imageView)
        //imageView.setBackgroundResource(imagem)


        viewFlipper.addView(imageView)
        viewFlipper.setFlipInterval(10000)
        viewFlipper.setAutoStart(true)
        viewFlipper.startFlipping()

        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left)
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right)
    }

    private fun flipperImageCapa(imagem: String) {

        imageVC = ImageView(this)
        Picasso.get().load(imagem).into(imageVC)
        //imageView.setBackgroundResource(imagem)


        viewFlipperCapa.addView(imageVC)
        viewFlipperCapa.setFlipInterval(2000)
        viewFlipperCapa.setAutoStart(true)

        viewFlipperCapa.setInAnimation(this, android.R.anim.slide_in_left)
        viewFlipperCapa.setOutAnimation(this, android.R.anim.slide_out_right)
    }

    private fun removeAnuncio(codAnuncio: String) {

        val rel = FirebaseDatabase.getInstance().getReference("Anuncios")
        rel.child(codAnuncio)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Anúncio eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
            }

    }


}
