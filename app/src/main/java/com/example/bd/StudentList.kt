package com.example.bd

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
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

class StudentList : AppCompatActivity() , OnStudentClickListener{

    private lateinit var StdListAdapter: studentListAdapter
    private lateinit var actionBar: ActionBar

    //arraylist para o holder
    private lateinit var anunciosArrayList:ArrayList<studentList>

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_student_list)

        //configure actionbar
        //actionBar = supportActionBar!!
        //actionBar.title = getString(R.string.studentAdList)

//  Show recycler on screen
        //StdListAdapter = studentListAdapter(ArrayList())
        //val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        //recyclerView.adapter = StdListAdapter
        //recyclerView.layoutManager = LinearLayoutManager(this)


        StdListAdapter = studentListAdapter(ArrayList(), this)
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StdListAdapter
        StdListAdapter.notifyDataSetChanged()

        firebaseAuth = FirebaseAuth.getInstance()

        //carrega os anuncios
        anunciosArrayList = arrayListOf<studentList>()
        StdListAdapter.rmAll()
        loadList()

        //Bottom menu
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        //seleciona o item do menu
        bottomNavigationView.setSelectedItemId(R.id.home)

        MyApplication.bottomMenu(bottomNavigationView, this)

        //Ativa o modo imersivo
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        //Dark mode
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun loadList() {
        //carrega o anuncio
        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()) {
                       for (anuncioSnap in snapshot.children) {
                           val visiblidade = "${anuncioSnap.child("visiblidade").value}"

                           if(visiblidade.equals("1")){ //verifica se o anuncio está no estado 1
                               val anuncio = anuncioSnap.getValue(studentList::class.java)
                               anunciosArrayList.add(anuncio!!)
                           }
                       }
                        //carrega para view
                       anunciosArrayList.forEach{
                           StdListAdapter.addTodo(it)
                       }
               }
            }

            override fun onCancelled(error: DatabaseError) {

            }

            fun onComplete() {
                finish()
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
}
