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
import com.example.bd.app.MyApplication
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
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            startActivity(Intent(this, PrimeiraActivity::class.java))
            finish()
        }

        StdListAdapter = studentFavListAdapter(ArrayList(), this)
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StdListAdapter
        StdListAdapter.rmAll()
        StdListAdapter.notifyDataSetChanged()

        //carrega os anuncios
        loadList()

        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.favourite)

        //ao clicar em um item
        MyApplication.bottomMenu(bottomNavigationView, this)

    }

    private fun loadList() {
        val codigos = ArrayList<String>()

        val referencia = FirebaseDatabase.getInstance().getReference("Utilizadores")
        referencia.child(firebaseAuth.uid!!).child("Favoritos")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    anunciosArrayList = arrayListOf<studentList>()
                    StdListAdapter.rmAll()

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

                                    if(anuncio!=null) {
                                        StdListAdapter.addTodo(anuncio!!)
                                    }
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
