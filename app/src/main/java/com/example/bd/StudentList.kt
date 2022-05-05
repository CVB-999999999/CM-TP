package com.example.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.adapters.studentListAdapter
import com.example.bd.models.studentList

class StudentList : AppCompatActivity() {

    private lateinit var StdListAdapter: studentListAdapter
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_student_list)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = getString(R.string.studentAdList)

//  Show recycler on screen
        StdListAdapter = studentListAdapter(ArrayList())
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.adapter = StdListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//  Data Filler
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Avenida do Altantico nº123 1234-567 - Viana do Castelo",
                200F,
                false,
                false,
                true,
                1,
                1
            )
        )
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Quarto com cama de casal, cozinha e casa de banho privativa",
                200F,
                false,
                true,
                false,
                2,
                2
            )
        )
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Avenida do Altantico nº123 1234-567 - Viana do Castelo",
                200F,
                true,
                false,
                true,
                0,
                3
            )
        )
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Quarto com cama de casal, cozinha e casa de banho privativa",
                200F,
                true,
                true,
                true,
                1,
                4
            )
        )
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Avenida do Altantico nº123 1234-567 - Viana do Castelo",
                200F,
                false,
                false,
                false,
                2,
                5
            )
        )
    }
}