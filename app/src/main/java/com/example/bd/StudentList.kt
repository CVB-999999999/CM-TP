package com.example.bd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.adapters.studentListAdapter
import com.example.bd.models.studentList

class StudentList : AppCompatActivity() {

    private lateinit var StdListAdapter: studentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_student_list)

//  Show recycler on screen
        StdListAdapter = studentListAdapter(ArrayList())
        val recyclerView: RecyclerView = findViewById(R.id.stdLine)
        recyclerView.adapter = StdListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//  Data Filler
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Quarto com cama de casal, cozinha e casa de banho privativa",
                200F,
                false,
                false,
                true,
                12F
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
                30F
            )
        )
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Quarto com cama de casal, cozinha e casa de banho privativa",
                200F,
                true,
                false,
                true,
                120F
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
                2F
            )
        )
        StdListAdapter.addTodo(
            studentList(
                "Casa perto da ESTG",
                "Quarto com cama de casal, cozinha e casa de banho privativa",
                200F,
                false,
                false,
                false,
                5F
            )
        )
    }
}