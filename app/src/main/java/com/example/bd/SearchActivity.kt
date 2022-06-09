package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.adapters.studentListAdapter
import com.example.bd.app.MyApplication
import com.example.bd.app.OnStudentClickListener
import com.example.bd.models.studentList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity(), OnStudentClickListener {

    private lateinit var StdListAdapter: studentListAdapter
    private lateinit var anunciosArrayList: ArrayList<studentList>
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        StdListAdapter = studentListAdapter(ArrayList(), this)
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StdListAdapter
        StdListAdapter.notifyDataSetChanged()

        firebaseAuth = FirebaseAuth.getInstance()

        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.search)

        MyApplication.bottomMenu(bottomNavigationView, this)

        // --- Dropdown Stuff   --- //
        // --- Shared Room
        var spinner: Spinner = findViewById(R.id.shroom)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.sharedRoom,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        // --- Accessible ---
        spinner = findViewById(R.id.acc)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.accessible,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        // --- Gender ---
        spinner = findViewById(R.id.gender)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    fun pesquisa(view: View) {
        anunciosArrayList = arrayListOf<studentList>()
        StdListAdapter.rmAll()

        val et = findViewById<EditText>(R.id.pesquisa)
        val string = et.text.toString()

        val spinner1 = findViewById<Spinner>(R.id.shroom)
        val spinner2 = findViewById<Spinner>(R.id.acc)
        val spinner3 = findViewById<Spinner>(R.id.gender)

        // Seleciona a tabela
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        // Live Update
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Vai carregar todos os dados
                    for (anuncioSnap in snapshot.children) {
                        val visiblidade = "${anuncioSnap.child("visiblidade").value}"
                        val titulo = "${anuncioSnap.child("titulo").value}"
                        val morada = "${anuncioSnap.child("morada").value}"
                        val g = "${anuncioSnap.child("reservado").value}"
                        var a = "${anuncioSnap.child("rAcessivel").value}"
                        val sh = "${anuncioSnap.child("rAcessivel").value}"

                        // TODO fix lina 120!!!!!

                        // Bool para Int
                        a = if (a == "true" || a == "1") {
                            0.toString();
                        } else {
                            1.toString();
                        }

                        // Verifica se o anuncio está no estado 1 e se segue os criterios de pesquisa
                        if (visiblidade == "1" && (titulo.contains(
                                string,
                                ignoreCase = true
                            ) || morada.contains(
                                string,
                                ignoreCase = true
                            ))
                        ) {
                            // Verfica os filtros
                            if (spinner2.selectedItemId.toString().trim().contains(
                                    a.trim(),
                                    ignoreCase = true
                                ) &&
                                spinner3.selectedItemId.toString() == g
                            ) {
                                val anuncio = anuncioSnap.getValue(studentList::class.java)
                                anunciosArrayList.add(anuncio!!)
                            }
                        }
                    }
                    //carrega para view
                    anunciosArrayList.forEach {
                        StdListAdapter.addTodo(it)
                    }
                }
            }

            //  ERRO
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onStudentClickItem(position: Int) {
        val anuncioNome = anunciosArrayList[position].titulo
        val codA = anunciosArrayList[position].codAnuncio

        Toast.makeText(this, "Anúncio: " + anuncioNome, Toast.LENGTH_LONG).show()

        val intent = Intent(this, VerAnuncio::class.java)
        intent.putExtra("codAnuncio", codA)
        startActivity(intent)
    }

    fun resize(view: View) {

        val ll = findViewById<LinearLayout>(R.id.ll2)
        val lp = ll.getLayoutParams()

//        Toast.makeText(this, lp.height.toString(), Toast.LENGTH_SHORT).show()

        // Verifies the state of the dropdown
        if (lp.height == 0) {
            lp.height = 400;
        } else {
            lp.height = 0;
        }

        ll.setLayoutParams(lp)
    }
}
