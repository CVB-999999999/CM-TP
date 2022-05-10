package com.example.bd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.adapters.studentFavListAdapter
import com.example.bd.adapters.studentListAdapter
import com.example.bd.app.OnStudentClickCodListener
import com.example.bd.models.studentList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class StdFavoritosList : AppCompatActivity(), OnStudentClickCodListener {
    private lateinit var StdListAdapter: studentFavListAdapter
    private lateinit var actionBar: ActionBar

    private var TAG = "onChange"
    //arraylist para o holder
    private lateinit var anunciosArrayList: ArrayList<studentList>

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_std_favoritos_list)

        firebaseAuth = FirebaseAuth.getInstance()

        StdListAdapter = studentFavListAdapter(ArrayList(), this)
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StdListAdapter
        StdListAdapter.notifyDataSetChanged()

        //carrega os anuncios
        anunciosArrayList = arrayListOf<studentList>()
        loadList()

        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.favourite)

        //ao clicar em um item
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    startActivity(Intent(this, Defenicoes::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.home -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favourite -> {
                    startActivity(Intent(this, StdFavoritosList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.qrCode -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.search -> {
                    startActivity(Intent(this, StudentList::class.java))
                    overridePendingTransition(0, 0)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        //Ativa o modo imersivo
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

    }

    private fun loadList() {
        val codigos = ArrayList<String>()

        val referencia = FirebaseDatabase.getInstance().getReference("Utilizadores")
        referencia.child(firebaseAuth.uid!!).child("Favoritos")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (anuncioSnap in snapshot.children) {
                            val cod = "${anuncioSnap.child("codAnuncio").value}"
                            codigos.add(cod)
                        }
                    }

                    for (i in codigos) {
                        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
                        ref.child(i)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    val anuncio = snapshot.getValue(studentList::class.java)
                                    //anunciosArrayList.add(anuncio!!)
                                    Log.d(TAG, "onDataChange: entra" + anunciosArrayList.size)

                                    StdListAdapter.addTodo(anuncio!!)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.d(TAG, "onDataChange: cancela")
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onStudentClickCodItem(codA: String) {

        val intent = Intent(this, VerAnuncio::class.java)
        intent.putExtra("codAnuncio", codA)
        startActivity(intent)
    }
}
