package com.example.bd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        // --- --- Shared Room  --- --- //
        // get reference to the string array that we just created
        var languages = resources.getStringArray(R.array.sharedRoom)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        var arrayAdapter = ArrayAdapter(this, R.layout.filter_partilhado, languages)
        // get reference to the autocomplete text view
        val qp = findViewById<AutoCompleteTextView>(R.id.qp)
        // set adapter to the autocomplete tv to the arrayAdapter
        qp.setAdapter(arrayAdapter)

        // --- --- Shared Room  --- --- //
        // get reference to the string array that we just created
        languages = resources.getStringArray(R.array.accessible)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        arrayAdapter = ArrayAdapter(this, R.layout.filter_partilhado, languages)
        // get reference to the autocomplete text view
        val a = findViewById<AutoCompleteTextView>(R.id.a)
        // set adapter to the autocomplete tv to the arrayAdapter
        a.setAdapter(arrayAdapter)

        // --- --- Shared Room  --- --- //
        // get reference to the string array that we just created
        languages = resources.getStringArray(R.array.gender)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        arrayAdapter = ArrayAdapter(this, R.layout.filter_partilhado, languages)
        // get reference to the autocomplete text view
        val g = findViewById<AutoCompleteTextView>(R.id.g)
        // set adapter to the autocomplete tv to the arrayAdapter
        g.setAdapter(arrayAdapter)
    }

    fun pesquisa(view: View) {
        anunciosArrayList = arrayListOf<studentList>()
        StdListAdapter.rmAll()

        val et = findViewById<EditText>(R.id.pesquisa)
        val string = et.text.toString()

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

                        // Verifica se o anuncio está no estado 1
                        if (visiblidade.equals("1") && (titulo.contains(
                                string,
                                ignoreCase = true
                            ) || morada.contains(string, ignoreCase = true))
                        ) {
                            val anuncio = anuncioSnap.getValue(studentList::class.java)
                            anunciosArrayList.add(anuncio!!)
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
            lp.height = 650;
        } else {
            lp.height = 0;
        }

        ll.setLayoutParams(lp)
    }
}
