package com.example.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.adapters.studentListAdapter
import com.example.bd.models.studentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentList : AppCompatActivity() {

    private lateinit var StdListAdapter: studentListAdapter
    private lateinit var actionBar: ActionBar

    //arraylist para o holder
    private lateinit var anunciosArrayList:ArrayList<studentList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_student_list)

        //configure actionbar
        //actionBar = supportActionBar!!
        //actionBar.title = getString(R.string.studentAdList)

//  Show recycler on screen
        StdListAdapter = studentListAdapter(ArrayList())
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.adapter = StdListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        //carrega os anuncios
        anunciosArrayList = arrayListOf<studentList>()
        loadList()

//  Data Filler
        //StdListAdapter.addTodo(
        //    studentList(
        //        "11111",
        //        111111111,
        //        1111,
        //        "1111",
        //        "1111",
        //        "1111",
        //        "1111",
        //        "1111",
        //        true,
        //        true,
        //        true,
        //        true,
        //        "1111",
        //        "1111",
        //        "1111",
        //        "1"
        //    )
        //)
    }

    private fun loadList() {

        val ref = FirebaseDatabase.getInstance().getReference("Anuncios")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if (snapshot.exists()) {
                       for (anuncioSnap in snapshot.children) {
                           val anuncio = anuncioSnap.getValue(studentList::class.java)
                           anunciosArrayList.add(anuncio!!)
                       }
                        //carrega para view
                       anunciosArrayList.forEach{
                           StdListAdapter.addTodo(it)
                       }
               }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
